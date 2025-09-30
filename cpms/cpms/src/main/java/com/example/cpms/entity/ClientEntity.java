package com.example.cpms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public UserEntity getUser() {
        return user;
    }

    // ===== SETTERS (THIS IS WHAT WAS MISSING!) =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}