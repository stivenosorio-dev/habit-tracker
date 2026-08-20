package com.stivenosorio_dev.habit_tracker_backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class UserResponse {
    private String id;
    private String displayName;
    private String email;
    private int xpTotal;
    private int level;
    private Instant createdAt;
}
