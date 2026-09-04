package com.rtucare.backend.DTO.response;

import com.rtucare.backend.entity.Period;
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

    public static PeriodDTO from(Period period) {
        return new PeriodDTO(period.getId(), period.getUserProfile().getUser().getId(),
                period.getStartDate(), period.getEndDate(), period.getFlowIntensity(),
                period.getSymptoms() == null ? List.of() : period.getSymptoms(), period.getNotes());
    }
}
