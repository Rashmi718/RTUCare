package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.UserProfileRequestDTO;
import com.rtucare.backend.DTO.request.UserProfileUpdateDTO;
import com.rtucare.backend.DTO.response.UserProfileResponseDTO;
import com.rtucare.backend.DTO.response.UserProfileUpdateResponseDTO;
import com.rtucare.backend.services.UserProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rtucare/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO> createProfile(@PathVariable long id,
                                                                @Valid @RequestBody UserProfileRequestDTO dto) {
        logger.info("Creating profile for user id: {}", id);
        return ResponseEntity.ok(userProfileService.createProfile(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable long id) {
        logger.info("Fetching profile for user id: {}", id);
        return ResponseEntity.ok(userProfileService.getProfileView(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserProfileUpdateResponseDTO> updateProfile(@PathVariable long id,
                                                                      @Valid @RequestBody UserProfileUpdateDTO dto) {
        logger.info("Update profile request for user id: {}", id);
        return ResponseEntity.ok(userProfileService.updateProfile(id, dto));
    }
}
