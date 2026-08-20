package com.stivenosorio_dev.habit_tracker_backend.service;

import com.stivenosorio_dev.habit_tracker_backend.dto.UserResponse;
import com.stivenosorio_dev.habit_tracker_backend.exception.ResourceNotFoundException;
import com.stivenosorio_dev.habit_tracker_backend.model.User;
import com.stivenosorio_dev.habit_tracker_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getCurrentUser(String userId)
            throws ExecutionException, InterruptedException {
        User user = userRepository.findById(userId);
        if (user == null) throw new ResourceNotFoundException("Usuario no encontrado");
        return UserResponse.builder().id(user.getId()).displayName(user.getDisplayName())
                .email(user.getEmail()).xpTotal(user.getXpTotal()).level(user.getLevel())
                .createdAt(user.getCreatedAt()).build();
    }
}
