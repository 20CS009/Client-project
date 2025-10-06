package com.example.cpms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String companyName;


}