package com.example.cpms.service;

import com.example.cpms.entity.UserEntity;
import com.example.cpms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

            return new User(user.getEmail(), user.getPassword(), Collections.singletonList(authority));
        } catch (UsernameNotFoundException e) {
            throw e; // Re-throw UsernameNotFoundException as-is
        } catch (Exception e) {
            throw new RuntimeException("Failed to load user by email: " + e.getMessage());
        }
    }
}