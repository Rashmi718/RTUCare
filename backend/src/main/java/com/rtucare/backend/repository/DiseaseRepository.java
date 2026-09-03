package com.rtucare.backend.repository;

import com.rtucare.backend.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    List<Disease> findByMedicalHistoryId(Long medicalHistoryId);
}
