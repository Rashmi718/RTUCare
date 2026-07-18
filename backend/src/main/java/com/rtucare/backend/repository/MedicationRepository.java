package com.rtucare.backend.repository;

import com.rtucare.backend.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByMedicalHistoryId(Long medicalHistoryId);
}
