package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.PeriodRequestDTO;
import com.rtucare.backend.DTO.request.SymptomLogRequestDTO;
import com.rtucare.backend.DTO.response.PeriodDTO;
import com.rtucare.backend.DTO.response.SymptomLogDTO;
import com.rtucare.backend.services.PeriodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rtucare/period")
public class PeriodController {

    private final PeriodService periodService;

    public PeriodController(PeriodService periodService) {
        this.periodService = periodService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<PeriodDTO> createPeriod(@PathVariable long userId,
                                                  @Valid @RequestBody PeriodRequestDTO dto) {
        return ResponseEntity.ok(periodService.createPeriod(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PeriodDTO>> getPeriods(@PathVariable long userId) {
        return ResponseEntity.ok(periodService.getPeriods(userId));
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<PeriodDTO> getPeriod(@PathVariable long id) {
        return ResponseEntity.ok(periodService.getPeriod(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodDTO> updatePeriod(@PathVariable long id,
                                                  @Valid @RequestBody PeriodRequestDTO dto) {
        return ResponseEntity.ok(periodService.updatePeriod(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePeriod(@PathVariable long id) {
        periodService.deletePeriod(id);
        return ResponseEntity.ok("Period deleted successfully.");
    }

    @PostMapping("/{periodId}/symptoms")
    public ResponseEntity<SymptomLogDTO> addSymptom(@PathVariable long periodId,
                                                    @Valid @RequestBody SymptomLogRequestDTO dto) {
        return ResponseEntity.ok(periodService.addSymptom(periodId, dto));
    }

    @GetMapping("/{periodId}/symptoms")
    public ResponseEntity<List<SymptomLogDTO>> getSymptoms(@PathVariable long periodId) {
        return ResponseEntity.ok(periodService.getSymptoms(periodId));
    }

    @PutMapping("/{periodId}/symptoms/{symptomId}")
    public ResponseEntity<SymptomLogDTO> updateSymptom(@PathVariable long periodId,
                                                       @PathVariable long symptomId,
                                                       @Valid @RequestBody SymptomLogRequestDTO dto) {
        return ResponseEntity.ok(periodService.updateSymptom(symptomId, dto));
    }

    @DeleteMapping("/{periodId}/symptoms/{symptomId}")
    public ResponseEntity<String> deleteSymptom(@PathVariable long periodId,
                                                @PathVariable long symptomId) {
        periodService.deleteSymptom(symptomId);
        return ResponseEntity.ok("Symptom log deleted successfully.");
    }
}
