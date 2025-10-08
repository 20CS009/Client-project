//package com.example.cpms.dto;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class RegisterRequest {
//    @NotBlank(message = "Name is required")
//    private String name;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Email should be valid")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    private String password;
//}

package com.example.cpms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}