// File: src/main/java/com/example/cpms/controller/ProjectController.java
package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.cpms.dto.ProjectResponseDto; // Make sure this import is present

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // --- ADD PROJECT ---
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDto>> addProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ProjectResponseDto> response = projectService.addProject(request, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- GET SINGLE PROJECT BY ID ---
    @GetMapping("/{id}") // Maps to GET /api/projects/{id}
    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ProjectResponseDto> response = projectService.getProjectById(id, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- UPDATE PROJECT ---
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ProjectResponseDto> response = projectService.updateProject(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- GET ALL PROJECTS ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getAllProjects(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<List<ProjectResponseDto>> response = projectService.getAllProjects(currentUser);
        return ResponseEntity.ok(response);
    }

    // --- GET PROJECTS BY CLIENT ID ---
    // Maps to GET /api/projects/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByClientId(
            @PathVariable Long clientId,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<List<ProjectResponseDto>> response = projectService.getProjectsByClientId(clientId, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- DELETE PROJECT ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<String> response = projectService.deleteProject(id, currentUser);
        return ResponseEntity.ok(response);
    }
}