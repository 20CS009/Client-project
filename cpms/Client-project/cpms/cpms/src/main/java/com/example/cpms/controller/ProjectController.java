//package com.example.cpms.controller;
//
//import com.example.cpms.dto.ApiResponse;
//import com.example.cpms.dto.DeleteConfirmationDto;
//import com.example.cpms.dto.ProjectRequest;
//import com.example.cpms.dto.ProjectResponseDto;
//import com.example.cpms.entity.ProjectEntity;
//import com.example.cpms.entity.UserEntity;
//import com.example.cpms.service.ProjectService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/projects")
//public class ProjectController {
//
//    @Autowired
//    private ProjectService projectService;
//
//    @PostMapping
//    public ResponseEntity<ApiResponse<ProjectResponseDto>> addProject(
//            @Valid @RequestBody ProjectRequest request,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<ProjectResponseDto> response = projectService.addProject(request, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(
//            @PathVariable Long id,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<ProjectResponseDto> response = projectService.getProjectById(id, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
//            @PathVariable Long id,
//            @Valid @RequestBody ProjectRequest request,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<ProjectResponseDto> response = projectService.updateProject(id, request, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getAllProjects(Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<List<ProjectResponseDto>> response = projectService.getAllProjects(currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//    // In ProjectController.java
//    @GetMapping("/client/{clientId}")
//    public ResponseEntity<ApiResponse<List<ProjectEntity>>> getProjectsByClient(
//            @PathVariable Long clientId,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        // This should now work because we added the method to ProjectService
//        ApiResponse<List<ProjectEntity>> response = projectService.getProjectsByClient(clientId, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<DeleteConfirmationDto>> deleteProject(
//            @PathVariable Long id,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<DeleteConfirmationDto> response = projectService.deleteProject(id, currentUser);
//        return ResponseEntity.ok(response);
//    }
//}

package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.dto.ProjectResponseDto;
import com.example.cpms.entity.ProjectEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDto>> addProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            ProjectEntity savedProject = projectService.addProject(request, currentUser);
            ProjectResponseDto projectDto = modelMapper.map(savedProject, ProjectResponseDto.class);
            return ResponseEntity.ok(ApiResponse.success("Project added successfully", projectDto));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByClient(
            @PathVariable Long clientId,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            List<ProjectEntity> projects = projectService.getProjectsByClient(clientId, currentUser);
            List<ProjectResponseDto> projectDtos = projects.stream()
                    .map(project -> modelMapper.map(project, ProjectResponseDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Projects for client retrieved successfully", projectDtos));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getAllProjects(Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            List<ProjectEntity> projects = projectService.getAllProjects(currentUser);
            List<ProjectResponseDto> projectDtos = projects.stream()
                    .map(project -> modelMapper.map(project, ProjectResponseDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("All projects retrieved successfully", projectDtos));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            ProjectEntity updatedProject = projectService.updateProject(id, request, currentUser);
            ProjectResponseDto projectDto = modelMapper.map(updatedProject, ProjectResponseDto.class);
            return ResponseEntity.ok(ApiResponse.success("Project updated successfully", projectDto));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            projectService.deleteProject(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Project deleted successfully", null));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }
}
