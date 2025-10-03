// File: src/main/java/com/example/cpms/service/ProjectService.java
package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.entity.*;
import com.example.cpms.exception.AccessDeniedException;
import com.example.cpms.exception.ResourceNotFoundException;
import com.example.cpms.repository.ClientRepository;
import com.example.cpms.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    public ApiResponse<ProjectEntity> addProject(ProjectRequest request, UserEntity user) {
        // 1. Find client
        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));

        // 2. Check ownership (if user is not ADMIN)
        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            // Use custom exception for access denied
            throw new AccessDeniedException("You don't own this client with id: " + request.getClientId());
        }

        // 3. Create project
        ProjectEntity project = new ProjectEntity();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(request.getStatus());
        project.setClient(client);
        project.setUser(user); // Link to current user for filtering

        ProjectEntity savedProject = projectRepository.save(project);
        return ApiResponse.success("Project added successfully", savedProject);
    }

    public ApiResponse<List<ProjectEntity>> getProjectsByClient(Long clientId, UserEntity user) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            // Use custom exception for access denied
            throw new AccessDeniedException("Access denied - You don't have permission to access projects for this client");
        }

        List<ProjectEntity> projects = projectRepository.findByClient_Id(clientId);
        return ApiResponse.success("Projects retrieved successfully", projects);
    }

    public ApiResponse<List<ProjectEntity>> getAllProjects(UserEntity user) {
        try {
            List<ProjectEntity> projects;
            if (user.getRole() == Role.ADMIN) {
                projects = projectRepository.findAll();
            } else {
                projects = projectRepository.findByUser(user);
            }
            return ApiResponse.success("All projects retrieved successfully", projects);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get all projects: " + e.getMessage());
        }
    }

    // --- NEW CRUD METHODS ---

    /**
     * Updates an existing project.
     */
    public ApiResponse<ProjectEntity> updateProject(Long id, ProjectRequest request, UserEntity user) {
        // 1. Find the existing project
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        // 2. Check ownership of the project's client
        if (user.getRole() != Role.ADMIN && !project.getClient().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't own the client for this project");
        }

        // 3. Find the new client (if clientId is being changed)
        ClientEntity newClient = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));

        // 4. Check ownership of the new client
        if (user.getRole() != Role.ADMIN && !newClient.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't own the new client with id: " + request.getClientId());
        }

        // 5. Update project fields
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setStatus(request.getStatus());
        project.setClient(newClient); // Update client association if needed

        // 6. Save and return success response
        ProjectEntity updatedProject = projectRepository.save(project);
        return ApiResponse.success("Project updated successfully", updatedProject);
    }

    /**
     * Deletes an existing project.
     */
    public ApiResponse<String> deleteProject(Long id, UserEntity user) {
        // 1. Find the existing project
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        // 2. Check ownership of the project's client
        if (user.getRole() != Role.ADMIN && !project.getClient().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't own the client for this project");
        }

        // 3. Delete the project
        projectRepository.delete(project);

        // 4. Return success response
        return ApiResponse.success("Project deleted successfully", null);
    }
}