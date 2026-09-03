package com.rtucare.backend.repository;

import com.rtucare.backend.entity.PhysicalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhysicalStatusRepository extends JpaRepository<PhysicalStatus, Long> {

    Optional<PhysicalStatus> findByUserProfileId(Long userProfileId);
}
