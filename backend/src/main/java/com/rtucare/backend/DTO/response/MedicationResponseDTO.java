package com.rtucare.backend.DTO.response;

import com.rtucare.backend.entity.Medication;
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

    private Boolean isOngoing;

    public static MedicationResponseDTO from(Medication medication) {
        return new MedicationResponseDTO(medication.getId(), medication.getMedicalHistory().getId(),
                medication.getName(), medication.getDosage(), medication.getDurationMonths(),
                medication.getIsOngoing());
    }
}
