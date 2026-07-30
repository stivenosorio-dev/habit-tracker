package com.stivenosorio_dev.habit_tracker_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitLog {
    private String id;
    private String habitId;
    private String userId;
    private String date;
    private boolean completed;
    private int xpEarned;
}
