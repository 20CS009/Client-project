package com.example.cpms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

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

    public ClientEntity getClient() {
        return client;
    }

    public UserEntity getUser() {
        return user;
    }

    // ===== SETTERS (THIS IS WHAT WAS MISSING!) =====
    public void setId(Long id) {
        this.id = id;
    }

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

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}