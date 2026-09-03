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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.rtucare.backend.entity.User;

@Slf4j
@Service
public class UserProfileService {

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
        log.info("Creating profile for user id: {}", id);
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
        log.info("Profile created successfully with id: {}", saved.getId());
        return toDTO(saved);
    }

    public UserProfileResponseDTO getProfile(long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user id: " + userId));
        return toDTO(profile);
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

    private UserProfileResponseDTO toDTO(UserProfile profile) {
        PhysicalStatus ps = profile.getPhysicalStatus();
        HeightUnit displayUnit = ps.getHeightUnit();
        return new UserProfileResponseDTO(profile.getId(), profile.getUser().getId(),
                fromCm(ps.getHeightCm(), displayUnit), displayUnit, ps.getWeightKg(),
                ps.getAge(), ps.getLocation());
    }

    public UserProfileUpdateResponseDTO updateProfile(long userId, UserProfileUpdateDTO dto) {
        log.info("Updating profile for user id: {}", userId);
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
        log.info("Profile updated successfully with id: {}", updated.getId());
        return toUpdateDTO(updated);
    }

    private UserProfileUpdateResponseDTO toUpdateDTO(UserProfile profile) {
        PhysicalStatus ps = profile.getPhysicalStatus();
        HeightUnit displayUnit = ps.getHeightUnit();
        return new UserProfileUpdateResponseDTO(profile.getId(), profile.getUser().getId(),
                fromCm(ps.getHeightCm(), displayUnit), displayUnit, ps.getWeightKg(),
                ps.getAge(), ps.getLocation());
    }
}
