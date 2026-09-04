package com.rtucare.backend.DTO.response;

import com.rtucare.backend.entity.MedicalHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryResponseDTO {

    private Long id;
    private Long userId;
    private Boolean hasMedicalIssues;
    private List<DiseaseResponseDTO> diseases;
    private List<MedicationResponseDTO> medications;

    public static MedicalHistoryResponseDTO from(MedicalHistory medicalHistory) {
        List<DiseaseResponseDTO> diseases = medicalHistory.getDiseases() == null ? List.of()
                : medicalHistory.getDiseases().stream().map(DiseaseResponseDTO::from).toList();
        List<MedicationResponseDTO> medications = medicalHistory.getMedications() == null ? List.of()
                : medicalHistory.getMedications().stream().map(MedicationResponseDTO::from).toList();
        return new MedicalHistoryResponseDTO(medicalHistory.getId(),
                medicalHistory.getUserProfile().getUser().getId(),
                medicalHistory.getHasMedicalIssues(), diseases, medications);
    }
}
