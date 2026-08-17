package com.stivenosorio_dev.habit_tracker_backend.service;

import com.stivenosorio_dev.habit_tracker_backend.dto.HabitRequest;
import com.stivenosorio_dev.habit_tracker_backend.dto.HabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.model.Habit;
import com.stivenosorio_dev.habit_tracker_backend.repository.HabitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitService habitService;

    @Test
    void crearHabito_debeGuardarYDevolverElHabitoCreado() throws Exception {
        HabitRequest request = new HabitRequest();
        request.setName("Meditar");
        request.setDescription("10 minutos al despertar");

        Habit habitGuardado = new Habit();
        habitGuardado.setId("abc123");
        habitGuardado.setName("Meditar");

        when(habitRepository.save(any(Habit.class))).thenReturn(habitGuardado);

        HabitResponse resultado = habitService.crearHabito("user1", request);

        assertEquals("abc123", resultado.getId());
        assertEquals("Meditar", resultado.getName());
    }
}