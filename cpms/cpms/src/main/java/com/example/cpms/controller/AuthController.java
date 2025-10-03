package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.LoginRequest;
import com.example.cpms.dto.RegisterRequest;
import com.example.cpms.security.JwtUtil;
import com.example.cpms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            ApiResponse<String> response = userService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            // 1. Load user details by email
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            // 2. Check if password matches
            if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid email or password"));
            }

            // 3. If credentials are valid, generate JWT token
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(ApiResponse.success("Login successful", new JwtResponse(jwt)));
        } catch (UsernameNotFoundException e) {
            // User not found
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email or password"));
        } catch (Exception e) {
            // Any other unexpected error
            System.err.println("Login failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }

    // Helper class for JWT response
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