package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.UserProfileRequestDTO;
import com.rtucare.backend.DTO.request.UserProfileUpdateDTO;
import com.rtucare.backend.DTO.response.MedicalHistoryResponseDTO;
import com.rtucare.backend.DTO.response.UserProfileResponseDTO;
import com.rtucare.backend.DTO.response.UserProfileUpdateResponseDTO;
import com.rtucare.backend.DTO.response.UserProfileViewDTO;
import com.rtucare.backend.entity.UserProfile;
import com.rtucare.backend.enums.HeightUnit;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.UserProfileRepository;
import com.rtucare.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.rtucare.backend.entity.User;

@Service
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private static final double CM_PER_FT = 30.48;

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final MedicalHistoryService medicalHistoryService;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository,
                              MedicalHistoryService medicalHistoryService) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.medicalHistoryService = medicalHistoryService;
    }

    public UserProfileResponseDTO createProfile(long id, UserProfileRequestDTO dto) {
        logger.info("Creating profile for user id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setHeightCm(toCm(dto.getHeight(), dto.getUnit()));
        userProfile.setHeightUnit(dto.getUnit());
        userProfile.setWeightKg(dto.getWeightKg());
        userProfile.setAge(dto.getAge());
        userProfile.setLocation(dto.getLocation());

        UserProfile saved = userProfileRepository.save(userProfile);
        logger.info("Profile created successfully with id: {}", saved.getId());
        return toDTO(saved, saved.getHeightUnit());
    }

    public UserProfileResponseDTO getProfile(long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
        return toDTO(profile, profile.getHeightUnit());
    }

    public UserProfileViewDTO getProfileView(long userId) {
        UserProfileResponseDTO profile = getProfile(userId);
        MedicalHistoryResponseDTO medicalHistory = medicalHistoryService.getMedicalHistory(userId);
        return new UserProfileViewDTO(userId, profile, medicalHistory);
    }

    private double toCm(double height, HeightUnit unit) {
        return unit == HeightUnit.FT ? height * CM_PER_FT : height;
    }

    private double fromCm(double heightCm, HeightUnit unit) {
        return unit == HeightUnit.FT ? heightCm / CM_PER_FT : heightCm;
    }

    private UserProfileResponseDTO toDTO(UserProfile profile, HeightUnit displayUnit) {
        return new UserProfileResponseDTO(profile.getId(), profile.getUser().getId(), fromCm(profile.getHeightCm(), displayUnit),
                displayUnit, profile.getWeightKg(), profile.getAge(), profile.getLocation());
    }

    public UserProfileUpdateResponseDTO updateProfile(long userId, UserProfileUpdateDTO dto) {
        logger.info("Updating profile for user id: {}", userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));

        userProfile.setHeightCm(toCm(dto.getHeight(), dto.getUnit()));
        userProfile.setHeightUnit(dto.getUnit());
        userProfile.setWeightKg(dto.getWeightKg());
        userProfile.setAge(dto.getAge());
        userProfile.setLocation(dto.getLocation());

        UserProfile updated = userProfileRepository.save(userProfile);
        logger.info("Profile updated successfully with id: {}", updated.getId());
        return toUpdateDTO(updated, updated.getHeightUnit());
    }

    private UserProfileUpdateResponseDTO toUpdateDTO(UserProfile profile, HeightUnit displayUnit) {
        return new UserProfileUpdateResponseDTO(profile.getId(), profile.getUser().getId(),
                fromCm(profile.getHeightCm(), displayUnit), displayUnit, profile.getWeightKg(),
                profile.getAge(), profile.getLocation());
    }
}
