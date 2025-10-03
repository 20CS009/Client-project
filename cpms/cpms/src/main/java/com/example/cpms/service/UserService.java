package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.LoginRequest;
import com.example.cpms.dto.RegisterRequest;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.repository.UserRepository;
import com.example.cpms.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


    public ApiResponse<JwtResponse> login(LoginRequest request) {
        try {
            // 1. Authenticate user credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2. If authentication successful, load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            // 3. Generate JWT token
            String jwt = jwtUtil.generateToken(userDetails);

            // 4. Return success response with token
            return ApiResponse.success("Login successful", new JwtResponse(jwt));
        } catch (Exception e) {
            // 5. Handle authentication failure
            return ApiResponse.error("Invalid email or password");
        }
    }


    public static class JwtResponse {
        private final String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}