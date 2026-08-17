package com.stivenosorio_dev.habit_tracker_backend.controller;

import com.stivenosorio_dev.habit_tracker_backend.dto.CompleteHabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.dto.HabitRequest;
import com.stivenosorio_dev.habit_tracker_backend.dto.HabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.service.HabitCompletionService;
import com.stivenosorio_dev.habit_tracker_backend.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/habits")
public class HabitController {
    private HabitService habitService;
    private final HabitCompletionService habitCompletionService;

    public HabitController(HabitService habitService, HabitCompletionService habitCompletionService) {
        this.habitService = habitService;
        this.habitCompletionService = habitCompletionService;
    }

    @PostMapping
    public ResponseEntity<HabitResponse> crear(@RequestBody @Valid HabitRequest request) throws ExecutionException, InterruptedException {

        String userId = "temporal-user-id";
        HabitResponse creado = habitService.crearHabito(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<HabitResponse>> listar() throws ExecutionException, InterruptedException {
        String userId = "temporal-user-id";
        return ResponseEntity.ok(habitService.listarHabitos(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> editar(
            @PathVariable String id,
            @RequestBody @Valid HabitRequest request) throws ExecutionException, InterruptedException {

        String userId = "temporal-user-id";
        return ResponseEntity.ok(habitService.editarHabito(userId, id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) throws ExecutionException, InterruptedException {
        String userId = "temporal-user-id";
        habitService.eliminarHabito(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<CompleteHabitResponse> completar(@PathVariable String id)
            throws ExecutionException, InterruptedException {
        String userId = "temporal-user-id";

        return ResponseEntity.ok(habitCompletionService.completar(userId, id));
    }


}
