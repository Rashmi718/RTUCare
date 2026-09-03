package com.rtucare.backend.repository;

import com.rtucare.backend.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    Optional<MedicalHistory> findByUserProfileUserId(Long userId);
}
