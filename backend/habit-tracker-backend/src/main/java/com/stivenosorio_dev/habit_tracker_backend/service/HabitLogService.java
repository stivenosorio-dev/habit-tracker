package com.stivenosorio_dev.habit_tracker_backend.service;

import com.stivenosorio_dev.habit_tracker_backend.dto.HabitLogResponse;
import com.stivenosorio_dev.habit_tracker_backend.exception.ResourceNotFoundException;
import com.stivenosorio_dev.habit_tracker_backend.model.Habit;
import com.stivenosorio_dev.habit_tracker_backend.model.HabitLog;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitLogRepository;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class HabitLogService {
    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;

    public HabitLogService(HabitRepository habitRepository, HabitLogRepository habitLogRepository) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
    }

    public List<HabitLogResponse> listarPorHabito(String userId, String habitId)
            throws ExecutionException, InterruptedException {
        Habit habit = habitRepository.findById(habitId);
        if (habit == null || !habit.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Hábito no encontrado");
        }
        return habitLogRepository.findByHabitIdAndUserId(habitId, userId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private HabitLogResponse toResponse(HabitLog log) {
        return HabitLogResponse.builder().id(log.getId()).habitId(log.getHabitId())
                .date(log.getDate()).completed(log.isCompleted()).xpEarned(log.getXpEarned()).build();
    }
}
