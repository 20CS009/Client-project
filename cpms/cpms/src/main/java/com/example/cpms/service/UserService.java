package com.example.cpms.service;

import com.example.cpms.dto.RegisterRequest;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER); // Default to USER
        return userRepository.save(user);
    }
}