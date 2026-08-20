package com.stivenosorio_dev.habit_tracker_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HabitLogResponse {
    private String id;
    private String habitId;
    private String date;
    private boolean completed;
    private int xpEarned;
}
