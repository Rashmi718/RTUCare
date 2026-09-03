package com.rtucare.backend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitsResponseDTO {

    private Long id;
    private Long userId;
    private Boolean smoker;
    private Boolean alcoholConsumer;
    private Boolean goodSleep;
    private Boolean balancedDiet;
    private Boolean exercise;
    private Boolean consumeExcessiveDrugs;
}
