package com.stivenosorio_dev.habit_tracker_backend.exception;

public class HabitAlreadyCompletedException extends RuntimeException {
    public HabitAlreadyCompletedException(String message) {
        super(message);
    }
}
