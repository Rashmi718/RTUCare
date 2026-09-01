package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.UserRegisterDTO;
import com.rtucare.backend.DTO.response.UserResponseDTO;
import com.rtucare.backend.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rtucare")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable long id) {
        logger.info("Fetching user with id: {}", id);
        return userService.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUserInfo(@PathVariable long id, @Valid @RequestBody UserRegisterDTO dto) {
        logger.info("Update request for user id: {}", id);
        userService.updateUser(id, dto);
        return ResponseEntity.ok("User updated successfully.");
    }
}
