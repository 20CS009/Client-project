package com.example.cpms.dto;

import com.example.cpms.entity.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private Long clientId; // Only send the ID of the associated client
    private String clientName; // Optionally, send the client's name for convenience

    // Static factory method for easy conversion (optional but helpful)
    public static ProjectResponseDto fromEntity(com.example.cpms.entity.ProjectEntity projectEntity) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(projectEntity.getId());
        dto.setTitle(projectEntity.getTitle());
        dto.setDescription(projectEntity.getDescription());
        dto.setStartDate(projectEntity.getStartDate());
        dto.setEndDate(projectEntity.getEndDate());
        dto.setStatus(projectEntity.getStatus());
        // Flatten the relationship to just the ID and name
        if (projectEntity.getClient() != null) {
            dto.setClientId(projectEntity.getClient().getId());
            dto.setClientName(projectEntity.getClient().getName());
        }
        return dto;
    }
}