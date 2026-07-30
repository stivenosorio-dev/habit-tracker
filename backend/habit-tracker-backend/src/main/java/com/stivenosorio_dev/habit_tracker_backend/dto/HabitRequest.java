package com.stivenosorio_dev.habit_tracker_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HabitRequest {

    @NotBlank(message = "El nombre del habito es obligatorio")
    private String name;

    private String description;
    private String category;

}
