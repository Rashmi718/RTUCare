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

    @NotNull(message = "Smoker field is required")
    private Boolean smoker;

    @NotNull(message = "Alcohol consumer field is required")
    private Boolean alcoholConsumer;

    private List<String> diseases;

    private List<MedicationRequestDTO> medications;
}
