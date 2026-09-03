package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.UserRegisterDTO;
import com.rtucare.backend.DTO.request.UserUpdateDTO;
import com.rtucare.backend.DTO.response.UserResponseDTO;
import com.rtucare.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rtucare/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id) {
        log.info("Fetching user with id: {}", id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @Valid @RequestBody UserRegisterDTO dto) {
        log.info("Update request for user id: {}", id);
        userService.updateUser(id, dto);
        return ResponseEntity.ok("User updated successfully.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> patchUser(@PathVariable long id, @Valid @RequestBody UserUpdateDTO dto) {
        log.info("Patch request for user id: {}", id);
        return ResponseEntity.ok(userService.partialUpdateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        log.info("Delete request for user id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
