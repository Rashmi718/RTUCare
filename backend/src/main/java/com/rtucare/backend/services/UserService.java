package com.rtucare.backend.services;

import com.rtucare.backend.DTO.UserRegisterDTO;
import com.rtucare.backend.DTO.UserResponseDTO;
import com.rtucare.backend.entity.User;
import com.rtucare.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserRegisterDTO dto){
        User u = new User();
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(u);
        return toDTO(u);
    }

    public void updateUser(long id , UserRegisterDTO dto){
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(u);
    }

    public  UserResponseDTO getUser(long id){
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found sorry !!!"));
        return toDTO(u);
    }

    public List<UserResponseDTO> getAllUser(){
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toDTO).toList();
    }

    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
    }
}
