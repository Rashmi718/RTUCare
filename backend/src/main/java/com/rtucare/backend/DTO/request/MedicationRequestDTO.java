package com.rtucare.backend.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationRequestDTO {

    @NotBlank(message = "Medication name is required")
    private String name;

    private String dosage;

    private Integer durationMonths;
}
