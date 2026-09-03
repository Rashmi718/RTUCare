package com.rtucare.backend.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryRequestDTO {

    @NotNull(message = "Medical issues field is required")
    private Boolean hasMedicalIssues;

    private List<DiseaseRequestDTO> diseases;

    private List<MedicationRequestDTO> medications;
}
