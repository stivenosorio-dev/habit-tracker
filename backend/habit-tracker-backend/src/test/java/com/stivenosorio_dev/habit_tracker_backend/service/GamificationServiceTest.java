package com.stivenosorio_dev.habit_tracker_backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 Tests unitarios de GamificationService.

 - Importante: esta clase NO toca Firestore ni ningún Repository — prueba
 exclusivamente la lógica de dominio pura (racha, XP, niveles), que es
 independiente de cómo o dónde se persisten los datos. Los escenarios de
 "el log queda bien guardado en Firestore" ya se validaron manualmente con
 Postman, y corresponden a un tipo de prueba distinto (integración), no a
 este archivo.
 **/

class GamificationServiceTest {

    private final GamificationService service = new GamificationService();


    // Primera vez que se completa un hábito
    @Nested
    @DisplayName("Primera vez completando un hábito")
    class PrimeraVez {

        @Test
        @DisplayName("La racha debe iniciar en 1 cuando no hay log previo")
        void primeraVezQueCompletaElHabito_debeIniciarRachaEn1() {
            int racha = service.calcularNuevaRacha(null, 0);
            assertEquals(1, racha);
        }

        @Test
        @DisplayName("El XP ganado la primera vez debe ser 11 (10 base + 1 de bonus de racha)")
        void primeraVez_xpDebeSer11() {
            int racha = service.calcularNuevaRacha(null, 0); // = 1
            int xp = service.calcularXpGanado(racha);
            assertEquals(11, xp);
        }
    }


    // Día consecutivo (racha sube)
    @Nested
    @DisplayName("Día consecutivo")
    class DiaConsecutivo {

        @Test
        @DisplayName("Completar un día después del último log debe incrementar la racha en 1")
        void completarUnDiaConsecutivo_debeIncrementarLaRacha() {
            LocalDate ayer = LocalDate.now().minusDays(1);
            int racha = service.calcularNuevaRacha(ayer, 1);
            assertEquals(2, racha);
        }

        @Test
        @DisplayName("Con racha en 2, el XP ganado debe ser 12 (10 base + 2 de bonus)")
        void rachaDe2_xpDebeSer12() {
            int xp = service.calcularXpGanado(2);
            assertEquals(12, xp);
        }

        @Test
        @DisplayName("Completar dos veces el mismo día no debe duplicar la racha")
        void completarDosVecesElMismoDia_noDebeDuplicarLaRacha() {
            LocalDate hoy = LocalDate.now();
            int racha = service.calcularNuevaRacha(hoy, 5);
            assertEquals(5, racha); // se mantiene igual, no sube ni se rompe
        }
    }


    // Racha rota (se saltó uno o más días)
    @Nested
    @DisplayName("Racha rota")
    class RachaRota {

        @Test
        @DisplayName("Saltarse un día debe reiniciar la racha a 1, sin importar qué tan alta era antes")
        void saltarseUnDia_debeReiniciarLaRachaA1() {
            LocalDate hace3Dias = LocalDate.now().minusDays(3);
            int racha = service.calcularNuevaRacha(hace3Dias, 10);
            assertEquals(1, racha);
        }

        @Test
        @DisplayName("Al reiniciarse la racha a 1, el XP ganado debe volver a ser 11")
        void rachaRota_xpVuelveA11() {
            LocalDate hace5Dias = LocalDate.now().minusDays(5);
            int racha = service.calcularNuevaRacha(hace5Dias, 20);
            int xp = service.calcularXpGanado(racha);

            assertEquals(1, racha);
            assertEquals(11, xp);
        }
    }

    // Tope de bonus de XP por racha (20)
    @Nested
    @DisplayName("Tope de bonus de XP por racha")
    class TopeDeXp {

        @ParameterizedTest(name = "racha={0} -> xpEsperado={1}")
        @CsvSource({
                "0, 10",    // sin racha, solo XP base
                "1, 11",
                "5, 15",    // racha de 5, bonus completo de 5
                "19, 29",   // justo antes del tope
                "20, 30",   // exactamente en el tope
                "21, 30",   // justo después del tope, ya no debe subir más
                "50, 30",   // muy por encima del tope
                "100, 30"   // racha absurdamente alta, sigue topado en 30
        })
        @DisplayName("El bonus de XP nunca debe superar 20, sin importar qué tan alta sea la racha")
        void calcularXpGanado_debeAplicarBonusConTope(int racha, int xpEsperado) {
            assertEquals(xpEsperado, service.calcularXpGanado(racha));
        }
    }


    // Sistema de niveles
    @Nested
    @DisplayName("Sistema de niveles")
    class SistemaDeNiveles {

        @Test
        @DisplayName("Con 0 XP, el usuario debe estar en nivel 1")
        void sinXp_debeEstarEnNivel1() {
            assertEquals(1, service.calcularNivel(0));
        }

        @Test
        @DisplayName("Justo un XP por debajo del requerido para nivel 2, NO debe subir de nivel")
        void xpJustoDebajo_noDebeSubirDeNivel() {
            int xpParaNivel2 = service.xpRequeridoParaNivel(2);
            int nivel = service.calcularNivel(xpParaNivel2 - 1);
            assertEquals(1, nivel);
        }

        @Test
        @DisplayName("Con el XP exacto requerido, debe subir a nivel 2")
        void xpExacto_debeSubirANivel2() {
            int xpParaNivel2 = service.xpRequeridoParaNivel(2);
            int nivel = service.calcularNivel(xpParaNivel2);
            assertEquals(2, nivel);
        }

        @Test
        @DisplayName("Con el XP exacto requerido para nivel 3, debe subir a nivel 3")
        void xpParaNivel3_debeSubirANivel3() {
            int xpParaNivel3 = service.xpRequeridoParaNivel(3);
            int nivel = service.calcularNivel(xpParaNivel3);
            assertEquals(3, nivel);
        }

        @Test
        @DisplayName("La curva de XP requerido debe ser creciente: nivel 3 exige más XP que nivel 2")
        void curvaDeProgresion_debeSerCreciente() {
            int xpNivel2 = service.xpRequeridoParaNivel(2);
            int xpNivel3 = service.xpRequeridoParaNivel(3);
            assertEquals(true, xpNivel3 > xpNivel2);
        }
    }
}