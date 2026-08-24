package com.rtucare.backend.DTO.response;

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
    private Boolean smoker;
    private Boolean alcoholConsumer;
    private List<String> diseases;
    private List<MedicationResponseDTO> medications;
}
