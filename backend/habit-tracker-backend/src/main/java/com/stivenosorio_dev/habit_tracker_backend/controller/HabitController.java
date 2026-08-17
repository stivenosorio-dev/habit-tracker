package com.stivenosorio_dev.habit_tracker_backend.controller;

import com.stivenosorio_dev.habit_tracker_backend.dto.HabitRequest;
import com.stivenosorio_dev.habit_tracker_backend.dto.HabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/habits")
public class HabitController {
    private HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<HabitResponse> crear(
            @RequestBody @Valid HabitRequest request,
            Authentication authentication) throws ExecutionException, InterruptedException {

        String userId = authentication.getName(); // viene del token, ya no es un valor fijo
        HabitResponse creado = habitService.crearHabito(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<HabitResponse>> listar(
            Authentication authentication) throws ExecutionException, InterruptedException {
        String userId = authentication.getName();
        return ResponseEntity.ok(habitService.listarHabitos(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> editar(
            @PathVariable String id,
            @RequestBody @Valid HabitRequest request,
            Authentication authentication) throws ExecutionException, InterruptedException {

        String userId = authentication.getName();
        return ResponseEntity.ok(habitService.editarHabito(userId, id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable String id,
            Authentication authentication) throws ExecutionException, InterruptedException {
        String userId = authentication.getName();
        habitService.eliminarHabito(userId, id);
        return ResponseEntity.noContent().build();
    }


}
