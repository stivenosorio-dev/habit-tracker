package com.stivenosorio_dev.habit_tracker_backend.service;

import com.stivenosorio_dev.habit_tracker_backend.exception.HabitAlreadyCompletedException;
import com.stivenosorio_dev.habit_tracker_backend.model.Habit;
import com.stivenosorio_dev.habit_tracker_backend.model.HabitLog;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitLogRepository;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitRepository;
import com.stivenosorio_dev.habit_tracker_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitCompletionServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitLogRepository habitLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GamificationService gamificationService;

    @InjectMocks
    private HabitCompletionService service;

    @Test
    void completar_dosVecesElMismoDia_debeRechazarLaSegunda() throws Exception {
        String userId = "user-1";
        String habitId = "habit-1";
        Habit habit = new Habit();
        habit.setId(habitId);
        habit.setUserId(userId);

        when(habitRepository.findById(habitId)).thenReturn(habit);
        when(habitLogRepository.findByHabitIdAndUserIdAndDate(
                org.mockito.ArgumentMatchers.eq(habitId),
                org.mockito.ArgumentMatchers.eq(userId),
                org.mockito.ArgumentMatchers.anyString()
        )).thenReturn(new HabitLog());

        assertThrows(
                HabitAlreadyCompletedException.class,
                () -> service.completar(userId, habitId)
        );

        verify(habitRepository, never()).save(org.mockito.ArgumentMatchers.any(Habit.class));
        verify(habitLogRepository, never()).save(org.mockito.ArgumentMatchers.any(HabitLog.class));
        verify(userRepository, never()).findById(org.mockito.ArgumentMatchers.anyString());
    }
}
