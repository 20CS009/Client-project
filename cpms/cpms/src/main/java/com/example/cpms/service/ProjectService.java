// File: src/main/java/com/example/cpms/service/ProjectService.java
package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.DeleteConfirmationDto;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.dto.ProjectResponseDto;
import com.example.cpms.entity.*;
import com.example.cpms.exception.AccessDeniedException;
import com.example.cpms.exception.ResourceNotFoundException;
import com.example.cpms.repository.ClientRepository;
import com.example.cpms.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors; // Add this import

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    // --- ADD PROJECT ---
    public ApiResponse<ProjectResponseDto> addProject(ProjectRequest request, UserEntity user) {
        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));

        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't own this client with id: " + request.getClientId());
        }

        ProjectEntity project = new ProjectEntity();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(request.getStatus());
        project.setClient(client);
        project.setUser(user); // Link to current user for filtering

        ProjectEntity savedProject = projectRepository.save(project);
        ProjectResponseDto projectDto = ProjectResponseDto.fromEntity(savedProject);
        return ApiResponse.success("Project added successfully", projectDto);
    }

    // --- GET SINGLE PROJECT BY ITS ID ---
    public ApiResponse<ProjectResponseDto> getProjectById(Long id, UserEntity user) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        // Check if user has permission to access this project (owns the client or is ADMIN)
        if (user.getRole() != Role.ADMIN && !project.getClient().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't own the client for this project");
        }

        ProjectResponseDto projectDto = ProjectResponseDto.fromEntity(project);
        return ApiResponse.success("Project retrieved successfully", projectDto);
    }

    // --- UPDATE PROJECT ---
    public ApiResponse<ProjectResponseDto> updateProject(Long id, ProjectRequest request, UserEntity user) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (user.getRole() != Role.ADMIN && !project.getClient().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't own the client for this project");
        }

        ClientEntity newClient = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));

        if (user.getRole() != Role.ADMIN && !newClient.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't own the new client with id: " + request.getClientId());
        }

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(request.getStatus());
        project.setClient(newClient); // Update client association if needed

        ProjectEntity updatedProject = projectRepository.save(project);
        ProjectResponseDto projectDto = ProjectResponseDto.fromEntity(updatedProject);
        return ApiResponse.success("Project updated successfully", projectDto);
    }

    // --- GET ALL PROJECTS (FILTERED BY USER ROLE) ---
    public ApiResponse<List<ProjectResponseDto>> getAllProjects(UserEntity user) {
        try {
            List<ProjectEntity> projects;
            if (user.getRole() == Role.ADMIN) {
                projects = projectRepository.findAll();
            } else {
                projects = projectRepository.findByUser(user);
            }
            // --- CONVERT LIST OF ENTITIES TO LIST OF DTOS ---
            List<ProjectResponseDto> projectDtos = projects.stream()
                    .map(ProjectResponseDto::fromEntity)
                    .collect(Collectors.toList()); // Use Collectors.toList()
            return ApiResponse.success("All projects retrieved successfully", projectDtos);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get all projects: " + e.getMessage());
        }
    }

    // --- GET PROJECTS BY CLIENT ID ---
    public ApiResponse<List<ProjectResponseDto>> getProjectsByClientId(Long clientId, UserEntity user) {
        // 1. Find the client to verify existence and ownership
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        // 2. Check if the current user can access this client's projects
        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't have permission to access projects for this client");
        }

        // 3. Fetch projects for the client
        List<ProjectEntity> projects = projectRepository.findByClient_Id(clientId);

        // 4. Convert to DTOs and return
        List<ProjectResponseDto> projectDtos = projects.stream()
                .map(ProjectResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ApiResponse.success("Projects for client retrieved successfully", projectDtos);
    }

    // --- DELETE PROJECT ---
    // In ProjectService.java
    public ApiResponse<DeleteConfirmationDto> deleteProject(Long id, UserEntity user) {
        try {
            ProjectEntity project = projectRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

            if (user.getRole() != Role.ADMIN && !project.getClient().getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied - You don't own the client for this project");
            }

            projectRepository.delete(project);


            return ApiResponse.success("Project deleted successfully", new DeleteConfirmationDto("Project deleted successfully"));
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete project: " + e.getMessage());
        }
    }


    public ApiResponse<List<ProjectEntity>> getProjectsByClient(Long clientId, UserEntity user) {
        try {
            // 1. Find the client
            ClientEntity client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

            // 2. Check ownership/access rights
            if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied - You don't have permission to access projects for this client");
            }

            // 3. Get projects for the client
            List<ProjectEntity> projects = projectRepository.findByClient_Id(clientId);

            // 4. Convert to DTOs (if using ModelMapper) or return entities
            return ApiResponse.success("Projects retrieved successfully", projects);
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions
            throw e;
        } catch (Exception e) {
            // Handle unexpected errors
            return ApiResponse.error("Failed to get projects by client: " + e.getMessage());
        }
    }
}