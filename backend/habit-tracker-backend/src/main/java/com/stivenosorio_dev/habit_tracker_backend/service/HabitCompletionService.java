package com.stivenosorio_dev.habit_tracker_backend.service;
import com.stivenosorio_dev.habit_tracker_backend.dto.CompleteHabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.exception.ResourceNotFoundException;
import com.stivenosorio_dev.habit_tracker_backend.model.Habit;
import com.stivenosorio_dev.habit_tracker_backend.model.HabitLog;
import com.stivenosorio_dev.habit_tracker_backend.model.User;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitLogRepository;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitRepository;
import com.stivenosorio_dev.habit_tracker_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Service
public class HabitCompletionService {
    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final UserRepository userRepository;
    private final GamificationService gamificationService;

    public HabitCompletionService(HabitRepository habitRepository,
                                  HabitLogRepository habitLogRepository,
                                  UserRepository userRepository,
                                  GamificationService gamificationService) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
        this.userRepository = userRepository;
        this.gamificationService = gamificationService;
    }

    public CompleteHabitResponse completar(String userId, String habitId)
            throws ExecutionException, InterruptedException {

        Habit habit = habitRepository.findById(habitId);
        if (habit == null || !habit.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Hábito no encontrado");
        }

        HabitLog ultimoLog = habitLogRepository.findLastByHabitId(habitId);
        LocalDate ultimaFecha = ultimoLog != null ? LocalDate.parse(ultimoLog.getDate()) : null;

        int nuevaRacha = gamificationService.calcularNuevaRacha(ultimaFecha, habit.getCurrentStreak());
        int xpGanado = gamificationService.calcularXpGanado(nuevaRacha);

        // Actualizar el hábito
        habit.setCurrentStreak(nuevaRacha);
        if (nuevaRacha > habit.getLongestStreak()) {
            habit.setLongestStreak(nuevaRacha);
        }
        habitRepository.save(habit);

        // Registrar el log del día
        HabitLog nuevoLog = new HabitLog();
        nuevoLog.setHabitId(habitId);
        nuevoLog.setUserId(userId);
        nuevoLog.setDate(LocalDate.now().toString());
        nuevoLog.setCompleted(true);
        nuevoLog.setXpEarned(xpGanado);
        habitLogRepository.save(nuevoLog);

        // Actualizar XP y nivel del usuario
        User user = userRepository.findById(userId);
        int nivelAnterior = user.getLevel();
        user.setXpTotal(user.getXpTotal() + xpGanado);
        int nuevoNivel = gamificationService.calcularNivel(user.getXpTotal());
        user.setLevel(nuevoNivel);
        userRepository.save(user);

        return CompleteHabitResponse.builder()
                .currentStreak(nuevaRacha)
                .xpEarned(xpGanado)
                .userXpTotal(user.getXpTotal())
                .userLevel(nuevoNivel)
                .leveledUp(nuevoNivel > nivelAnterior)
                .build();
    }
}
