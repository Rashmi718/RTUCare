package com.rtucare.backend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodDTO {

    private Long id;

    private Long userId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String flowIntensity;

    private List<String> symptoms;

    private String notes;
}
