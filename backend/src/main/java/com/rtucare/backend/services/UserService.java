package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.UserLoginDTO;
import com.rtucare.backend.DTO.request.UserRegisterDTO;
import com.rtucare.backend.DTO.response.UserLoginResponseDTO;
import com.rtucare.backend.DTO.response.UserResponseDTO;
import com.rtucare.backend.entity.User;
import com.rtucare.backend.exception.InvalidCredentialsException;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRegisterDTO dto) {
        logger.info("Creating new user with email: {}", dto.getEmail());
        User u = new User();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(u);
        logger.info("User created successfully with id: {}", u.getId());
        return UserResponseDTO.from(u);
    }

    public UserLoginResponseDTO login(UserLoginDTO dto) {
        logger.info("Login attempt for email: {}", dto.getEmail());
        User u = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), u.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        logger.info("User logged in successfully: {}", dto.getEmail());
        return new UserLoginResponseDTO(u.getId(), u.getName(), u.getEmail(), null, "Login successful");
    }

    public void updateUser(long id, UserRegisterDTO dto) {
        logger.info("Updating user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(u);
        logger.info("User updated successfully with id: {}", id);
    }

    public UserResponseDTO getUser(long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserResponseDTO.from(u);
    }

    public List<UserResponseDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponseDTO::from).toList();
    }

    public void deleteUser(long id) {
        logger.info("Deleting user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(u);
        logger.info("User deleted successfully with id: {}", id);
    }
}
