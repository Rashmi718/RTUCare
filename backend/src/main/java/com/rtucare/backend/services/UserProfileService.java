package com.rtucare.backend.services;

import com.rtucare.backend.DTO.UserProfileDTO;
import com.rtucare.backend.entity.UserProfile;
import com.rtucare.backend.enums.HeightUnit;
import com.rtucare.backend.repository.UserProfileRepository;
import com.rtucare.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.rtucare.backend.entity.User;

@Service
public class UserProfileService {

    private static final double CM_PER_FT = 30.48;

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository , UserRepository userRepository){
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileDTO createProfile(long id , UserProfileDTO dto){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("sorry user not registered !!!"));

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setHeightCm(toCm(dto.getHeight(), dto.getUnit()));
        userProfile.setHeightUnit(dto.getUnit());
        userProfile.setWeightKg(dto.getWeightKg());
        userProfile.setAge(dto.getAge());
        userProfile.setLocation(dto.getLocation());

        UserProfile saved = userProfileRepository.save(userProfile);
        return toDTO(saved, saved.getHeightUnit());
    }

    public UserProfileDTO getProfile(long userId){
        UserProfile profile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("profile not found"));
        return toDTO(profile, profile.getHeightUnit());
    }

    private double toCm(double height, HeightUnit unit) {
        return unit == HeightUnit.FT ? height * CM_PER_FT : height;
    }

    private double fromCm(double heightCm, HeightUnit unit) {
        return unit == HeightUnit.FT ? heightCm / CM_PER_FT : heightCm;
    }

    private UserProfileDTO toDTO(UserProfile profile, HeightUnit displayUnit) {
        return new UserProfileDTO(profile.getId(), profile.getUser().getId(), fromCm(profile.getHeightCm(), displayUnit),
                displayUnit, profile.getWeightKg(), profile.getAge(), profile.getLocation());
    }
}
