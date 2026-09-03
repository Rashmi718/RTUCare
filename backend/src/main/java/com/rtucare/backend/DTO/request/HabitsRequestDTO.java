package com.rtucare.backend.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitsRequestDTO {

    @NotNull(message = "Smoker field is required")
    private Boolean smoker;

    @NotNull(message = "Alcohol consumer field is required")
    private Boolean alcoholConsumer;

    @NotNull(message = "Good sleep field is required")
    private Boolean goodSleep;

    @NotNull(message = "Balanced diet field is required")
    private Boolean balancedDiet;

    @NotNull(message = "Exercise field is required")
    private Boolean exercise;

    @NotNull(message = "Consume excessive drugs field is required")
    private Boolean consumeExcessiveDrugs;
}
