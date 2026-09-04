package com.rtucare.backend.DTO.response;

import com.rtucare.backend.entity.Disease;
import com.rtucare.backend.entity.Medication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseResponseDTO {

    private Long id;
    private Long medicalHistoryId;
    private String name;
    private LocalDate diagnoseDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean stillOngoing;
    private MedicationResponseDTO medication;

    public static DiseaseResponseDTO from(Disease disease) {
        Medication med = disease.getMedication();
        MedicationResponseDTO medicationDTO = med == null ? null : MedicationResponseDTO.from(med);
        return new DiseaseResponseDTO(disease.getId(), disease.getMedicalHistory().getId(),
                disease.getName(), disease.getDiagnoseDate(), disease.getStartDate(),
                disease.getEndDate(), disease.getStillOngoing(), medicationDTO);
    }
}
