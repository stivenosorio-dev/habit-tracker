package com.stivenosorio_dev.habit_tracker_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habit {

    private String id;
    private String userId;
    private String name;
    private String description;
    private String category;
    private Instant  createdAt;
    private int currentStreak;
    private int longestStreak;
    private boolean active;
}
