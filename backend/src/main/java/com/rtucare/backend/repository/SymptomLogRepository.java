package com.rtucare.backend.repository;

import com.rtucare.backend.entity.SymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SymptomLogRepository extends JpaRepository<SymptomLog, Long> {

    List<SymptomLog> findByUserId(Long userId);

    List<SymptomLog> findByPeriodId(Long periodId);
}
