package com.stivenosorio_dev.habit_tracker_backend.controller;

import com.google.firebase.auth.FirebaseAuthException;

import com.stivenosorio_dev.habit_tracker_backend.dto.RegisterRequest;
import com.stivenosorio_dev.habit_tracker_backend.dto.UserResponse;
import com.stivenosorio_dev.habit_tracker_backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro de usuarios")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @Operation(summary = "Registrar un usuario")
    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request)
            throws FirebaseAuthException, ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }
}
