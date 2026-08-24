package com.rtucare.backend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileViewDTO {

    private Long userId;
    private UserProfileResponseDTO profile;
    private MedicalHistoryResponseDTO medicalHistory;
}
