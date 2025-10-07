//package com.example.cpms.service;
//
//import com.example.cpms.dto.*;
//import com.example.cpms.entity.Role;
//import com.example.cpms.entity.UserEntity;
//import com.example.cpms.exception.AccessDeniedException;
//import com.example.cpms.exception.ResourceAlreadyExistsException;
//import com.example.cpms.exception.ResourceException;
//import com.example.cpms.exception.ResourceNotFoundException;
//import com.example.cpms.repository.UserRepository;
//import com.example.cpms.security.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//    public void  register(RegisterRequest request) {
//
//            // Check if user already exists by email
//            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//                throw new ResourceAlreadyExistsException("User with email " + request.getEmail() + " already exists");
//            }
//
//            UserEntity user = new UserEntity();
//            user.setName(request.getName());
//            user.setEmail(request.getEmail());
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//            user.setRole(Role.USER); // Default to USER
//
//            userRepository.save(user);
//
//    }
//
//
//    public ApiResponse<LoginResponse> login(LoginRequest request) {
//        try {
//            Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());
//
//            UserEntity userEntity;
//            if(optionalUser.isPresent()) {
//                userEntity = optionalUser.get();
//            } else {
//                throw new ResourceNotFoundException("User Not Found");
//            }
//
//            boolean isValidaUser = checkUserCredentials(userEntity, request);
//            if (!isValidaUser) {
//                throw new AccessDeniedException("Please check the email address and password are entered wrong");
//            }
//
//            String emailAddress = userEntity.getEmail();
//            String userName = userEntity.getName();
//
//            String jwt = jwtUtil.generateToken(emailAddress, userName);
//
//            LoginResponse response = new LoginResponse();
//            response.setUserName(userName);
//            response.setEmailAddress(emailAddress);
//            response.setJwtResponse(new JwtResponse(jwt));
//            return ApiResponse.success("Login Successfully", response);
//        } catch (Exception e) {
//            throw new ResourceException("Invalid email or password");
//        }
//    }
//
//    private boolean checkUserCredentials(UserEntity userEntity, LoginRequest request) {
//        return passwordEncoder.matches(request.getPassword(), userEntity.getPassword());
//    }
//
//    public UserEntity getUserByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
//    }
//
//}


package com.example.cpms.service;

import com.example.cpms.dto.*;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.exception.ResourceAlreadyExistsException;
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

    // Throw exception for registration
    public ApiResponse<String> register(RegisterRequest request) {
        // Check if user already exists by email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return null;
    }

    // Return ApiResponse directly for login (no custom exceptions)
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        try {
            Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());

            // 1. Check if user exists
            if (optionalUser.isEmpty()) {
                return ApiResponse.error("Invalid email or password");
            }

            UserEntity userEntity = optionalUser.get();

            // 2. Check password
            boolean isValidUser = checkUserCredentials(userEntity, request);
            if (!isValidUser) {
                return ApiResponse.error("Invalid email or password"); // Return error ApiResponse
            }

            // 3. Generate JWT token
            String emailAddress = userEntity.getEmail();
            String userName = userEntity.getName();
            String jwt = jwtUtil.generateToken(emailAddress, userName);

            // 4. Create response
            LoginResponse response = new LoginResponse();
            response.setUserName(userName);
            response.setEmailAddress(emailAddress);
            response.setJwtResponse(new JwtResponse(jwt));

            return ApiResponse.success("Login successful", response);
        } catch (Exception e) {
            return ApiResponse.error("Login failed: " + e.getMessage()); //
        }
    }

    private boolean checkUserCredentials(UserEntity userEntity, LoginRequest request) {
        return passwordEncoder.matches(request.getPassword(), userEntity.getPassword());
    }

    // Throw exception for getUserByEmail (business logic)
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}