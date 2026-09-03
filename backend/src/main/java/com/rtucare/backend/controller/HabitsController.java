package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.HabitsRequestDTO;
import com.rtucare.backend.DTO.response.HabitsResponseDTO;
import com.rtucare.backend.services.HabitsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rtucare/habits")
public class HabitsController {

    private final HabitsService habitsService;

    public HabitsController(HabitsService habitsService) {
        this.habitsService = habitsService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<HabitsResponseDTO> createHabits(
            @PathVariable long userId,
            @Valid @RequestBody HabitsRequestDTO dto) {
        log.info("Creating habits for user id: {}", userId);
        return ResponseEntity.ok(habitsService.createHabits(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<HabitsResponseDTO> getHabits(@PathVariable long userId) {
        log.info("Fetching habits for user id: {}", userId);
        return ResponseEntity.ok(habitsService.getHabits(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HabitsResponseDTO> updateHabits(
            @PathVariable long userId,
            @Valid @RequestBody HabitsRequestDTO dto) {
        log.info("Updating habits for user id: {}", userId);
        return ResponseEntity.ok(habitsService.updateHabits(userId, dto));
    }
}
