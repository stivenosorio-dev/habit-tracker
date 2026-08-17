package com.stivenosorio_dev.habit_tracker_backend.controller;

import com.stivenosorio_dev.habit_tracker_backend.dto.CompleteHabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.dto.HabitRequest;
import com.stivenosorio_dev.habit_tracker_backend.dto.HabitResponse;
import com.stivenosorio_dev.habit_tracker_backend.service.HabitCompletionService;
import com.stivenosorio_dev.habit_tracker_backend.service.HabitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/habits")
@Tag(name = "Hábitos", description = "Gestión de hábitos del usuario")
public class HabitController {
    private HabitService habitService;
    private final HabitCompletionService habitCompletionService;

    public HabitController(HabitService habitService, HabitCompletionService habitCompletionService) {
        this.habitService = habitService;
        this.habitCompletionService = habitCompletionService;
    }

    @Operation(summary = "Crear un nuevo hábito")
    @ApiResponse(responseCode = "201", description = "Hábito creado exitosamente")
    @PostMapping
    public ResponseEntity<HabitResponse> crear(
            @RequestBody @Valid HabitRequest request,
            Authentication authentication) throws ExecutionException, InterruptedException {

        String userId = authentication.getName(); // viene del token, ya no es un valor fijo
        HabitResponse creado = habitService.crearHabito(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Listar hábitos del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de hábitos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<HabitResponse>> listar(
            Authentication authentication) throws ExecutionException, InterruptedException {
        String userId = authentication.getName();
        return ResponseEntity.ok(habitService.listarHabitos(userId));
    }

    @Operation(summary = "Editar un hábito existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hábito actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Hábito no encontrado o no pertenece al usuario")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> editar(
            @PathVariable String id,
            @RequestBody @Valid HabitRequest request,
            Authentication authentication) throws ExecutionException, InterruptedException {

        String userId = authentication.getName();
        return ResponseEntity.ok(habitService.editarHabito(userId, id, request));
    }


    @Operation(summary = "Completar un hábito hoy",
            description = "Calcula racha, XP y nivel del usuario a partir de este cumplimiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hábito completado, racha y XP actualizados"),
            @ApiResponse(responseCode = "404", description = "Hábito no encontrado o no pertenece al usuario")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable String id,
            Authentication authentication) throws ExecutionException, InterruptedException {
        String userId = authentication.getName();
        habitService.eliminarHabito(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<CompleteHabitResponse> completar(@PathVariable String id, Authentication authentication)
            throws ExecutionException, InterruptedException {
        String userId = authentication.getName();

        return ResponseEntity.ok(habitCompletionService.completar(userId, id));
    }

}
