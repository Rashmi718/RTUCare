package com.rtucare.backend.DTO.request;

import com.rtucare.backend.enums.HeightUnit;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDTO {

    @NotNull(message = "Height is required")
    @Min(value = 1, message = "Height must be positive")
    private Double height;

    @NotNull(message = "Height unit is required")
    private HeightUnit unit;

    @NotNull(message = "Weight is required")
    @Min(value = 1, message = "Weight must be positive")
    private Double weightKg;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must be realistic")
    private Integer age;

    private String location;
}
