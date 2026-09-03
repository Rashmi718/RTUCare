package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.HabitsRequestDTO;
import com.rtucare.backend.DTO.response.HabitsResponseDTO;
import com.rtucare.backend.services.HabitsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rtucare/habits")
public class HabitsController {

    private static final Logger logger = LoggerFactory.getLogger(HabitsController.class);

    private final HabitsService habitsService;

    public HabitsController(HabitsService habitsService) {
        this.habitsService = habitsService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<HabitsResponseDTO> createHabits(
            @PathVariable long userId,
            @Valid @RequestBody HabitsRequestDTO dto) {
        logger.info("Creating habits for user id: {}", userId);
        return ResponseEntity.ok(habitsService.createHabits(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<HabitsResponseDTO> getHabits(@PathVariable long userId) {
        logger.info("Fetching habits for user id: {}", userId);
        return ResponseEntity.ok(habitsService.getHabits(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<HabitsResponseDTO> updateHabits(
            @PathVariable long userId,
            @Valid @RequestBody HabitsRequestDTO dto) {
        logger.info("Updating habits for user id: {}", userId);
        return ResponseEntity.ok(habitsService.updateHabits(userId, dto));
    }
}
