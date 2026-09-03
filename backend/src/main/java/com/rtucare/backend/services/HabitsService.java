package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.HabitsRequestDTO;
import com.rtucare.backend.DTO.response.HabitsResponseDTO;
import com.rtucare.backend.entity.Habits;
import com.rtucare.backend.entity.UserProfile;
import com.rtucare.backend.exception.DuplicateResourceException;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.HabitsRepository;
import com.rtucare.backend.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitsService {

    private static final Logger logger = LoggerFactory.getLogger(HabitsService.class);

    private final HabitsRepository habitsRepository;
    private final UserProfileRepository userProfileRepository;

    public HabitsService(HabitsRepository habitsRepository, UserProfileRepository userProfileRepository) {
        this.habitsRepository = habitsRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public HabitsResponseDTO createHabits(long userId, HabitsRequestDTO dto) {
        logger.info("Creating habits for user id: {}", userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));

        if (habitsRepository.findByUserProfileId(userProfile.getId()).isPresent()) {
            throw new DuplicateResourceException("Habits already exists for user id: " + userId);
        }

        Habits habits = new Habits();
        habits.setUserProfile(userProfile);
        habits.setSmoker(dto.getSmoker());
        habits.setAlcoholConsumer(dto.getAlcoholConsumer());
        habits.setGoodSleep(dto.getGoodSleep());
        habits.setBalancedDiet(dto.getBalancedDiet());
        habits.setExercise(dto.getExercise());
        habits.setConsumeExcessiveDrugs(dto.getConsumeExcessiveDrugs());

        Habits saved = habitsRepository.save(habits);
        logger.info("Habits created successfully with id: {}", saved.getId());
        return toDTO(saved);
    }

    public HabitsResponseDTO getHabits(long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
        Habits habits = habitsRepository.findByUserProfileId(userProfile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habits not found for user id: " + userId));
        return toDTO(habits);
    }

    @Transactional
    public HabitsResponseDTO updateHabits(long userId, HabitsRequestDTO dto) {
        logger.info("Updating habits for user id: {}", userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
        Habits habits = habitsRepository.findByUserProfileId(userProfile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habits not found for user id: " + userId));

        habits.setSmoker(dto.getSmoker());
        habits.setAlcoholConsumer(dto.getAlcoholConsumer());
        habits.setGoodSleep(dto.getGoodSleep());
        habits.setBalancedDiet(dto.getBalancedDiet());
        habits.setExercise(dto.getExercise());
        habits.setConsumeExcessiveDrugs(dto.getConsumeExcessiveDrugs());

        Habits updated = habitsRepository.save(habits);
        logger.info("Habits updated successfully with id: {}", updated.getId());
        return toDTO(updated);
    }

    private HabitsResponseDTO toDTO(Habits habits) {
        return new HabitsResponseDTO(habits.getId(), habits.getUserProfile().getUser().getId(),
                habits.getSmoker(), habits.getAlcoholConsumer(), habits.getGoodSleep(),
                habits.getBalancedDiet(), habits.getExercise(), habits.getConsumeExcessiveDrugs());
    }
}
