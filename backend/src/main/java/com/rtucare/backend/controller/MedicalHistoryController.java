package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.MedicalHistoryRequestDTO;
import com.rtucare.backend.DTO.response.MedicalHistoryResponseDTO;
import com.rtucare.backend.services.MedicalHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rtucare/medical-history")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<MedicalHistoryResponseDTO> createMedicalHistory(
            @PathVariable long userId,
            @Valid @RequestBody MedicalHistoryRequestDTO dto) {
        return ResponseEntity.ok(medicalHistoryService.createMedicalHistory(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MedicalHistoryResponseDTO> getMedicalHistory(@PathVariable long userId) {
        return ResponseEntity.ok(medicalHistoryService.getMedicalHistory(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<MedicalHistoryResponseDTO> updateMedicalHistory(
            @PathVariable long userId,
            @Valid @RequestBody MedicalHistoryRequestDTO dto) {
        return ResponseEntity.ok(medicalHistoryService.updateMedicalHistory(userId, dto));
    }
}
