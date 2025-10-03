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

    // Static factory method for easy conversion (optional but helpful)
    public static ClientResponseDto fromEntity(com.example.cpms.entity.ClientEntity clientEntity) {
        ClientResponseDto dto = new ClientResponseDto();
        dto.setId(clientEntity.getId());
        dto.setName(clientEntity.getName());
        dto.setEmail(clientEntity.getEmail());
        dto.setPhone(clientEntity.getPhone());
        dto.setCompanyName(clientEntity.getCompanyName());
        return dto;
    }
}