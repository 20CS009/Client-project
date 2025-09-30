package com.example.cpms.dto;

import com.example.cpms.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class ProjectRequest {

    @NotBlank
    private String title;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private ProjectStatus status = ProjectStatus.PLANNED;

    @NotBlank
    private Long clientId; // ← This field must exist

    // ✅ GETTERS
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public Long getClientId() { // ✅ THIS WAS MISSING!
        return clientId;
    }

    // ✅ SETTERS
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}