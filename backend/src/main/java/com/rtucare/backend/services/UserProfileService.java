package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.UserProfileRequestDTO;
import com.rtucare.backend.DTO.request.UserProfileUpdateDTO;
import com.rtucare.backend.DTO.response.MedicalHistoryResponseDTO;
import com.rtucare.backend.DTO.response.UserProfileResponseDTO;
import com.rtucare.backend.DTO.response.UserProfileUpdateResponseDTO;
import com.rtucare.backend.DTO.response.UserProfileViewDTO;
import com.rtucare.backend.entity.PhysicalStatus;
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

        PhysicalStatus physicalStatus = new PhysicalStatus();
        physicalStatus.setUserProfile(userProfile);
        physicalStatus.setHeightCm(toCm(dto.getHeight(), dto.getUnit()));
        physicalStatus.setHeightUnit(dto.getUnit());
        physicalStatus.setWeightKg(dto.getWeightKg());
        physicalStatus.setAge(dto.getAge());
        physicalStatus.setLocation(dto.getLocation());
        userProfile.setPhysicalStatus(physicalStatus);

        UserProfile saved = userProfileRepository.save(userProfile);
        logger.info("Profile created successfully with id: {}", saved.getId());
        return UserProfileResponseDTO.from(saved);
    }

    public UserProfileResponseDTO getProfile(long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
        return UserProfileResponseDTO.from(profile);
    }

    public UserProfileViewDTO getProfileView(long userId) {
        UserProfileResponseDTO profile = getProfile(userId);
        MedicalHistoryResponseDTO medicalHistory = medicalHistoryService.getMedicalHistory(userId);
        return new UserProfileViewDTO(userId, profile, medicalHistory);
    }

    private double toCm(double height, HeightUnit unit) {
        return unit == HeightUnit.FT ? height * 30.48 : height;
    }

    public UserProfileUpdateResponseDTO updateProfile(long userId, UserProfileUpdateDTO dto) {
        logger.info("Updating profile for user id: {}", userId);
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));

        PhysicalStatus physicalStatus = userProfile.getPhysicalStatus();
        if (physicalStatus == null) {
            physicalStatus = new PhysicalStatus();
            physicalStatus.setUserProfile(userProfile);
            userProfile.setPhysicalStatus(physicalStatus);
        }

        physicalStatus.setHeightCm(toCm(dto.getHeight(), dto.getUnit()));
        physicalStatus.setHeightUnit(dto.getUnit());
        physicalStatus.setWeightKg(dto.getWeightKg());
        physicalStatus.setAge(dto.getAge());
        physicalStatus.setLocation(dto.getLocation());

        UserProfile updated = userProfileRepository.save(userProfile);
        logger.info("Profile updated successfully with id: {}", updated.getId());
        return UserProfileUpdateResponseDTO.from(updated);
    }
}
