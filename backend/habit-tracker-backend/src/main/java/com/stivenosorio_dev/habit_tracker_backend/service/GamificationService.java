package com.stivenosorio_dev.habit_tracker_backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GamificationService {

    private static final int XP_BASE = 10;
    private static final int TOPE_BONUS_RACHA = 20;

    /**
     * Calcula si la racha continúa, se reinicia o es el primer día.
     */
    public int calcularNuevaRacha(LocalDate ultimaFechaCompletado, int rachaActual) {
        if (ultimaFechaCompletado == null) {
            return 1; // primera vez que completa este hábito
        }
        LocalDate hoy = LocalDate.now();
        long diasDesdeUltimoCompletado = java.time.temporal.ChronoUnit.DAYS
                .between(ultimaFechaCompletado, hoy);

        if (diasDesdeUltimoCompletado == 1) {
            return rachaActual + 1; // día consecutivo
        } else if (diasDesdeUltimoCompletado == 0) {
            return rachaActual; // ya se completó hoy, no duplicar
        } else {
            return 1; // se rompió la racha, reinicia
        }
    }

    /**
     * XP ganado = base + bonus por racha (con tope).
     */
    public int calcularXpGanado(int rachaActual) {
        int bonus = Math.min(rachaActual, TOPE_BONUS_RACHA);
        return XP_BASE + bonus;
    }

    /**
     * XP acumulado necesario para alcanzar cierto nivel.
     * Curva: 100 * nivel^1.5
     */
    public int xpRequeridoParaNivel(int nivel) {
        return (int) (100 * Math.pow(nivel, 1.5));
    }

    /**
     * Dado el XP total actual, calcula en qué nivel debería estar el usuario.
     */
    public int calcularNivel(int xpTotal) {
        int nivel = 1;
        while (xpTotal >= xpRequeridoParaNivel(nivel + 1)) {
            nivel++;
        }
        return nivel;
    }
}