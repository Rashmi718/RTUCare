package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.UserProfileDTO;
import com.rtucare.backend.services.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rtucare/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createProfile(@PathVariable long id , @RequestBody UserProfileDTO dto){
        try{
            UserProfileDTO res = userProfileService.createProfile(id , dto);
            return ResponseEntity.ok(res);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
