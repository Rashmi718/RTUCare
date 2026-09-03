package com.rtucare.backend.services;

import com.rtucare.backend.DTO.request.UserLoginDTO;
import com.rtucare.backend.DTO.request.UserRegisterDTO;
import com.rtucare.backend.DTO.request.UserUpdateDTO;
import com.rtucare.backend.DTO.response.UserLoginResponseDTO;
import com.rtucare.backend.DTO.response.UserResponseDTO;
import com.rtucare.backend.entity.User;
import com.rtucare.backend.exception.InvalidCredentialsException;
import com.rtucare.backend.exception.ResourceNotFoundException;
import com.rtucare.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRegisterDTO dto) {
        log.info("Creating new user with email: {}", dto.getEmail());
        User u = new User();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(u);
        log.info("User created successfully with id: {}", u.getId());
        return toDTO(u);
    }

    public UserLoginResponseDTO login(UserLoginDTO dto) {
        log.info("Login attempt for email: {}", dto.getEmail());
        User u = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), u.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        log.info("User logged in successfully: {}", dto.getEmail());
        return new UserLoginResponseDTO(u.getId(), u.getName(), u.getEmail(), null, "Login successful");
    }

    public void updateUser(long id, UserRegisterDTO dto) {
        log.info("Updating user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(u);
        log.info("User updated successfully with id: {}", id);
    }

    public UserResponseDTO partialUpdateUser(long id, UserUpdateDTO dto) {
        log.info("Partially updating user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (dto.getName() != null) {
            u.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            u.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        User updated = userRepository.save(u);
        log.info("User updated successfully with id: {}", id);
        return toDTO(updated);
    }

    public UserResponseDTO getUser(long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toDTO(u);
    }

    public List<UserResponseDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toDTO).toList();
    }

    public void deleteUser(long id) {
        log.info("Deleting user with id: {}", id);
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(u);
        log.info("User deleted successfully with id: {}", id);
    }

    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }
}
