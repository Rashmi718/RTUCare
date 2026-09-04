package com.rtucare.backend.DTO.response;

import com.rtucare.backend.entity.SymptomLog;
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

    public static SymptomLogDTO from(SymptomLog symptomLog) {
        return new SymptomLogDTO(symptomLog.getId(), symptomLog.getUser().getId(),
                symptomLog.getPeriod() == null ? null : symptomLog.getPeriod().getId(),
                symptomLog.getDate(), symptomLog.getSymptom(), symptomLog.getSeverity(),
                symptomLog.getMood(), symptomLog.getNotes());
    }
}
