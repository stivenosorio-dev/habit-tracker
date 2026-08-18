package com.stivenosorio_dev.habit_tracker_backend.controller;

import com.stivenosorio_dev.habit_tracker_backend.dto.HabitLogResponse;
import com.stivenosorio_dev.habit_tracker_backend.service.HabitLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/habits")
@Tag(name = "Historial de hábitos", description = "Historial de cumplimiento de hábitos")
public class HabitLogController {
    private final HabitLogService habitLogService;
    public HabitLogController(HabitLogService habitLogService) { this.habitLogService = habitLogService; }

    @Operation(summary = "Listar el historial de un hábito")
    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<HabitLogResponse>> listar(
            @PathVariable String id, Authentication authentication)
            throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(habitLogService.listarPorHabito(authentication.getName(), id));
    }
}
