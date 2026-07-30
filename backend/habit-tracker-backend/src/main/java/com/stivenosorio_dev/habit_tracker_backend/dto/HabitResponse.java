package com.stivenosorio_dev.habit_tracker_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HabitResponse {

    private String id;
    private String name;
    private String description;
    private String category;
    private int currentStreak;
    private int longestStreak;
    private boolean active;
}
