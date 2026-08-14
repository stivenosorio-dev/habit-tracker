package com.stivenosorio_dev.habit_tracker_backend.service;

import com.stivenosorio_dev.habit_tracker_backend.dto.HabitRequest;
import com.stivenosorio_dev.habit_tracker_backend.dto.HabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.exception.ResourceNotFoundException;
import com.stivenosorio_dev.habit_tracker_backend.model.Habit;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public HabitResponse crearHabito(String userId, HabitRequest request) throws ExecutionException, InterruptedException {

        Habit  habit = new Habit();
        habit.setUserId(userId);
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setCategory(request.getCategory());
        habit.setCreatedAt(Instant.now());
        habit.setCurrentStreak(0);
        habit.setLongestStreak(0);
        habit.setActive(true);

        Habit guardado = habitRepository.save(habit);
        return toResponse(guardado);
    }

    public List<HabitResponse> listarHabitos(String userId) throws ExecutionException, InterruptedException {
        return habitRepository.findByUserId(userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public HabitResponse editarHabito(String userId, String habitId, HabitRequest request) throws ExecutionException, InterruptedException {
        Habit  habit = habitRepository.findById(habitId);
        if (habit == null || !habit.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Habito no encontrado");
        }

        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setCategory(request.getCategory());

        return toResponse(habitRepository.save(habit));
    }

    public void eliminarHabito(String userId, String habitId) throws ExecutionException, InterruptedException {

        Habit habit = habitRepository.findById(habitId);
        if (habit  == null || !habit.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Habito no encontrado");
        }

        habitRepository.deleteById(habitId);
    }

    public HabitResponse toResponse(Habit habit) {
        return HabitResponse.builder()
                .id(habit.getId())
                .name(habit.getName())
                .description(habit.getDescription())
                .category(habit.getCategory())
                .currentStreak(habit.getCurrentStreak())
                .longestStreak(habit.getLongestStreak())
                .active(habit.isActive())
                .build();
    }
}
