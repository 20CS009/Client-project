package com.example.cpms.service;

import com.example.cpms.dto.*;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.exception.AccessDeniedException;
import com.example.cpms.exception.ResourceException;
import com.example.cpms.exception.ResourceNotFoundException;
import com.example.cpms.repository.UserRepository;
import com.example.cpms.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public ApiResponse<String> register(RegisterRequest request) {
        try {
            // Check if user already exists by email
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


    public ApiResponse<LoginResponse> login(LoginRequest request) {
        try {
            Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());

            UserEntity userEntity;
            if(optionalUser.isPresent()) {
                userEntity = optionalUser.get();
            } else {
                throw new ResourceNotFoundException("User Not Found");
            }

            boolean isValidaUser = checkUserCredentials(userEntity, request);
            if (!isValidaUser) {
                throw new AccessDeniedException("Please check the email address and password are entered wrong");
            }

            String emailAddress = userEntity.getEmail();
            String userName = userEntity.getName();

            String jwt = jwtUtil.generateToken(emailAddress, userName);

            LoginResponse response = new LoginResponse();
            response.setUserName(userName);
            response.setEmailAddress(emailAddress);
            response.setJwtResponse(new JwtResponse(jwt));
            return ApiResponse.success("Login Successfully", response);
        } catch (Exception e) {
            throw new ResourceException("Invalid email or password");
        }
    }

    private boolean checkUserCredentials(UserEntity userEntity, LoginRequest request) {
        return passwordEncoder.matches(request.getPassword(), userEntity.getPassword());
    }

}