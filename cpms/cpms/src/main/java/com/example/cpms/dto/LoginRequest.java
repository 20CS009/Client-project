package com.example.cpms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    // ✅ Add getters (required for Spring to read JSON fields)
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Optional: setters (Spring uses them during JSON deserialization)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}