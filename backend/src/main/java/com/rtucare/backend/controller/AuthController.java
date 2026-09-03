package com.rtucare.backend.controller;

import com.rtucare.backend.DTO.request.UserLoginDTO;
import com.rtucare.backend.DTO.request.UserRegisterDTO;
import com.rtucare.backend.DTO.response.UserLoginResponseDTO;
import com.rtucare.backend.DTO.response.UserResponseDTO;
import com.rtucare.backend.services.JwtService;
import com.rtucare.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rtucare")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@Valid @RequestBody UserRegisterDTO dto) {
        log.info("Signup request for email: {}", dto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {
        log.info("Login request for email: {}", dto.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        UserLoginResponseDTO response = userService.login(dto);
        response.setToken(jwtService.generateToken(dto.getEmail()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null || !jwtService.isTokenValid(token)) {
            log.warn("Refresh request with invalid or blacklisted token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = jwtService.extractUsername(token);
        String newToken = jwtService.generateToken(email);
        log.info("Refreshed token for email: {}", email);
        return ResponseEntity.ok(new UserLoginResponseDTO(null, null, email, newToken, "Token refreshed"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (token == null) {
            log.warn("Logout request without a token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        jwtService.blacklist(token);
        log.info("Token blacklisted on logout");
        return ResponseEntity.ok("Logged out successfully.");
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
