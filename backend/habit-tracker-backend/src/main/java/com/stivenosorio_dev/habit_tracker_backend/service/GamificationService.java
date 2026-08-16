package com.stivenosorio_dev.habit_tracker_backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GamificationService {

    private static final int XP_BASE = 10;
    private static final int TOPE_BONUS_RACHA = 20;


    /* Calcula si la racha que lleva el usuario es el primer dia, si se debe reiniciar o si continua */

    public int calcularNuevaRacha(LocalDate ultimaFechaCompletado, int rachaActual) {
        if (ultimaFechaCompletado == null) {
            return 1;
        }
        LocalDate hoy = LocalDate.now();
        long diasDesdeUltimoCompletado = java.time.temporal.ChronoUnit.DAYS.between(ultimaFechaCompletado, hoy);

        if (diasDesdeUltimoCompletado == 1) {
            return rachaActual + 1; // día consecutivo
        } else if (diasDesdeUltimoCompletado == 0) {
            return rachaActual; // ya se completó hoy, no duplicar
        } else {
            return 1; // se rompió la racha, reinicia
        }
    }

    /*
    XP acumulado necesario para alcanzar el siguiente nivel.
    Formula: 100 * nivel^1.5
    */
    public int xpRequeridoParaNivel(int nivel){
        return (int) (100*Math.pow(nivel, 1.5));
    }

    public int calcularNivel (int xpTotal){
        int nivel =1;
        while (xpTotal >= xpRequeridoParaNivel(nivel+1)) {
            nivel++;
        }
        return nivel;
    }

}
