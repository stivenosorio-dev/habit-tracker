package com.stivenosorio_dev.habit_tracker_backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.stivenosorio_dev.habit_tracker_backend.dto.RegisterRequest;
import com.stivenosorio_dev.habit_tracker_backend.dto.UserResponse;
import com.stivenosorio_dev.habit_tracker_backend.model.User;
import com.stivenosorio_dev.habit_tracker_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest request)
            throws FirebaseAuthException, ExecutionException, InterruptedException {
        UserRecord.CreateRequest firebaseRequest = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword())
                .setDisplayName(request.getDisplayName());
        UserRecord firebaseUser = FirebaseAuth.getInstance().createUser(firebaseRequest);

        User user = new User(firebaseUser.getUid(), request.getDisplayName(), request.getEmail(),
                0, 1, Instant.now());
        return toResponse(userRepository.save(user));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder().id(user.getId()).displayName(user.getDisplayName())
                .email(user.getEmail()).xpTotal(user.getXpTotal()).level(user.getLevel())
                .createdAt(user.getCreatedAt()).build();
    }
}
