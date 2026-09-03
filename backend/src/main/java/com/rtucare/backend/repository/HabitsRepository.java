package com.rtucare.backend.repository;

import com.rtucare.backend.entity.Habits;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HabitsRepository extends JpaRepository<Habits, Long> {

    Optional<Habits> findByUserProfileId(Long userProfileId);
}
