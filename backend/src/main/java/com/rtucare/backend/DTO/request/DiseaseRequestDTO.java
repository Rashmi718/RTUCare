package com.rtucare.backend.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseRequestDTO {

    @NotBlank(message = "Disease name is required")
    private String name;

    private LocalDate diagnoseDate;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Still ongoing field is required")
    private Boolean stillOngoing;

    private MedicationRequestDTO medication;
}
