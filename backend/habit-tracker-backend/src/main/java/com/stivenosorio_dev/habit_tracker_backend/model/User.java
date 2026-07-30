package com.stivenosorio_dev.habit_tracker_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String displayName;
    private String email;
    private int xpTotal;
    private int level;
    private Instant createdAt;
}
