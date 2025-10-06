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
import com.example.cpms.dto.DeleteConfirmationDto;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.dto.ProjectResponseDto;
import com.example.cpms.entity.ProjectEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ProjectService;
import com.example.cpms.service.UserService; //Add this import
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; //Add this import
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor //Use constructor injection
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService; //Inject UserService to load UserEntity

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponseDto>> addProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        //  Load full UserEntity from database using email
        String userEmail = authentication.getName();
        UserEntity currentUser = userService.getUserByEmail(userEmail);

        ApiResponse<ProjectResponseDto> response = projectService.addProject(request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> getProjectById(
            @PathVariable Long id,
            Authentication authentication) {
        // Load full UserEntity from database using email
        String userEmail = authentication.getName();
        UserEntity currentUser = userService.getUserByEmail(userEmail);

        ApiResponse<ProjectResponseDto> response = projectService.getProjectById(id, currentUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDto>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        // Load full UserEntity from database using email
        String userEmail = authentication.getName();
        UserEntity currentUser = userService.getUserByEmail(userEmail);

        ApiResponse<ProjectResponseDto> response = projectService.updateProject(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getAllProjects(Authentication authentication) {
        //Load full UserEntity from database using email
        String userEmail = authentication.getName();
        UserEntity currentUser = userService.getUserByEmail(userEmail);

        ApiResponse<List<ProjectResponseDto>> response = projectService.getAllProjects(currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<ProjectResponseDto>>> getProjectsByClient(
            @PathVariable Long clientId,
            Authentication authentication) {
        // Load full UserEntity from database using email
        String userEmail = authentication.getName();
        UserEntity currentUser = userService.getUserByEmail(userEmail);

        ApiResponse<List<ProjectResponseDto>> response = projectService.getProjectsByClient(clientId, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        // Load full UserEntity from database using email
        String userEmail = authentication.getName();
        UserEntity currentUser = userService.getUserByEmail(userEmail);

        ApiResponse<String> response = projectService.deleteProject(id, currentUser);
        return ResponseEntity.ok(response);
    }
}