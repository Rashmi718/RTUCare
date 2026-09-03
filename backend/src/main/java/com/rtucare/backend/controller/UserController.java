package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.UserLoginDTO;
import com.rtucare.backend.DTO.request.UserRegisterDTO;
import com.rtucare.backend.DTO.response.UserLoginResponseDTO;
import com.rtucare.backend.DTO.response.UserResponseDTO;
import com.rtucare.backend.services.JwtService;
import com.rtucare.backend.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rtucare")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@Valid @RequestBody UserRegisterDTO dto) {
        logger.info("Signup request for email: {}", dto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {
        logger.info("Login request for email: {}", dto.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        UserLoginResponseDTO response = userService.login(dto);
        response.setToken(jwtService.generateToken(dto.getEmail()));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id) {
        logger.info("Fetching user with id: {}", id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @Valid @RequestBody UserRegisterDTO dto) {
        logger.info("Update request for user id: {}", id);
        userService.updateUser(id, dto);
        return ResponseEntity.ok("User updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        logger.info("Delete request for user id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
