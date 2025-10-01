package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.RegisterRequest;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<String> register(RegisterRequest request) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ApiResponse.error("User with email " + request.getEmail() + " already exists");
            }

            UserEntity user = new UserEntity();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER); // Default to USER

            userRepository.save(user);
            return ApiResponse.success("User registered successfully", null);
        } catch (Exception e) {
            return ApiResponse.error("Failed to register user: " + e.getMessage());
        }
    }
}