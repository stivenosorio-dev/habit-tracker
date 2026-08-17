package com.stivenosorio_dev.habit_tracker_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompleteHabitResponse {
    private int currentStreak;
    private int xpEarned;
    private int userXpTotal;
    private int userLevel;
    private boolean leveledUp;
}