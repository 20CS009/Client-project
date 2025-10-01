package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.entity.ProjectEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")

public class ProjectController {

    private final ProjectService projectService;


    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectEntity>> addProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ProjectEntity savedProject = projectService.addProject(request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Project added successfully", savedProject));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ProjectEntity>>> getProjectsByClient(
            @PathVariable Long clientId,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        List<ProjectEntity> projects = projectService.getProjectsByClient(clientId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved successfully", projects));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectEntity>>> getAllProjects(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        List<ProjectEntity> projects = projectService.getAllProjects(currentUser);
        return ResponseEntity.ok(ApiResponse.success("All projects retrieved successfully", projects));
    }
}