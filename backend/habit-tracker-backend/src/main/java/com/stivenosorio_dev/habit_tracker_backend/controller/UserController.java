package com.stivenosorio_dev.habit_tracker_backend.controller;

import com.stivenosorio_dev.habit_tracker_backend.dto.UserResponse;
import com.stivenosorio_dev.habit_tracker_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Información del usuario autenticado")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @Operation(summary = "Obtener el perfil del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication authentication)
            throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(userService.getCurrentUser(authentication.getName()));
    }
}
