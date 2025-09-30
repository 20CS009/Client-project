package com.example.cpms.controller;

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
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectEntity> addProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ProjectEntity savedProject = projectService.addProject(request, currentUser);
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProjectEntity>> getProjectsByClient(
            @PathVariable Long clientId,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        List<ProjectEntity> projects = projectService.getProjectsByClient(clientId, currentUser);
        return ResponseEntity.ok(projects);
    }

    @GetMapping
    public ResponseEntity<List<ProjectEntity>> getAllProjects(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        List<ProjectEntity> projects = projectService.getAllProjects(currentUser);
        return ResponseEntity.ok(projects);
    }
}