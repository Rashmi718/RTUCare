package com.rtucare.backend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SymptomLogDTO {

    private Long id;

    private Long userId;

    private Long periodId;

    private LocalDate date;

    private String symptom;

    private Integer severity;

    private String mood;

    private String notes;
}
