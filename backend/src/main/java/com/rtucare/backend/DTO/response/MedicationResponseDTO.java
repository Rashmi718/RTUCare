package com.rtucare.backend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationResponseDTO {

    private Long id;

    private Long medicalHistoryId;

    private String name;

    private String dosage;

    private Integer durationMonths;
}
