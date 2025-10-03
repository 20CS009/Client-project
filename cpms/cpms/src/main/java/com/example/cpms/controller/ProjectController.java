
package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.entity.ProjectEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectEntity>> addProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ProjectEntity> response = projectService.addProject(request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ProjectEntity>>> getProjectsByClient(
            @PathVariable Long clientId,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<List<ProjectEntity>> response = projectService.getProjectsByClient(clientId, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectEntity>>> getAllProjects(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<List<ProjectEntity>> response = projectService.getAllProjects(currentUser);
        return ResponseEntity.ok(response);
    }

    // Update ENDPOINTS ---

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectEntity>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ProjectEntity> response = projectService.updateProject(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    /*** Deletes an existing project.*/
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<String> response = projectService.deleteProject(id, currentUser);
        return ResponseEntity.ok(response);
    }
}