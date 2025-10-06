//package com.example.cpms.controller;
//
//import com.example.cpms.dto.ApiResponse;
//import com.example.cpms.dto.LoginRequest;
//import com.example.cpms.dto.LoginResponse;
//import com.example.cpms.dto.RegisterRequest;
//import com.example.cpms.security.JwtUtil;
//import com.example.cpms.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
//        try {
//            ApiResponse<String> response = userService.register(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
//        ApiResponse<LoginResponse> apiResponse = userService.login(request);
//        return ResponseEntity.status(HttpStatus.OK.value()).body(apiResponse);
//    }
//
//}

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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor  
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid email or password"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(ApiResponse.success("Login successful", new JwtResponse(jwt)));
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
