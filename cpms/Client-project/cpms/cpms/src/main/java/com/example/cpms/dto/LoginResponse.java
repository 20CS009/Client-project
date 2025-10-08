package com.example.cpms.dto;
import com.example.cpms.controller.AuthController;
import lombok.Getter;
import lombok.Setter;
import lombok.*;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String userName;

    private String emailAddress;

    private JwtResponse jwtResponse;


}



