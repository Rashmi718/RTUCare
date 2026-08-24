package com.rtucare.backend.DTO.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SymptomLogRequestDTO {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Symptom is required")
    private String symptom;

    @NotNull(message = "Severity is required")
    @Min(value = 1, message = "Severity must be at least 1")
    @Max(value = 10, message = "Severity must be at most 10")
    private Integer severity;

    private String mood;

    private String notes;
}
