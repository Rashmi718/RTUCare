package com.rtucare.backend.repository;

import com.rtucare.backend.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodRepository extends JpaRepository<Period, Long> {

    List<Period> findByUserId(Long userId);
}
