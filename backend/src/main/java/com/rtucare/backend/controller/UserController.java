package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.UserRegisterDTO;
import com.rtucare.backend.DTO.UserResponseDTO;
import com.rtucare.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rtucare")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRegisterDTO dto){
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public  UserResponseDTO getUserById(@PathVariable long id){
        return userService.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUserInfo(@PathVariable long id , @RequestBody UserRegisterDTO dto){
        try {
            userService.updateUser(id , dto);
            return ResponseEntity.ok("User updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
