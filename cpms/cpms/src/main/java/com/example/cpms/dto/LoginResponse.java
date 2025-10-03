package com.example.cpms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String userName;

    private String emailAddress;

    private JwtResponse jwtResponse;
}
