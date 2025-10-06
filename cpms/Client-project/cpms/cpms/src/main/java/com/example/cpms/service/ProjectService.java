package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.dto.ProjectResponseDto;
import com.example.cpms.entity.*;
import com.example.cpms.exception.AccessDeniedException;
import com.example.cpms.exception.ResourceNotFoundException;
import com.example.cpms.repository.ClientRepository;
import com.example.cpms.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;

    /**
     * Adds a new project for a specific client.
     *
     * @param request The project data from the request body.
     * @param user    The currently authenticated user.
     * @return ApiResponse containing the saved project or an error message.
     */
    public ApiResponse<ProjectResponseDto> addProject(ProjectRequest request, UserEntity user) {
        try {
            // 1. Find client
            ClientEntity client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));

            // 2. Check ownership (if user is not ADMIN)
            if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("You don't own this client with id: " + request.getClientId());
            }

            // 3. ✅ Check for duplicate project title for the same client
            if (projectRepository.existsByClient_IdAndTitle(request.getClientId(), request.getTitle())) {
                return ApiResponse.error("Project with title '" + request.getTitle() + "' already exists for this client");
            }

            // 4. Create project
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
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            return ApiResponse.error("Failed to add project: " + e.getMessage());
        }
    }

    /**
     * Retrieves a specific project by ID, enforcing ownership rules.
     *
     * @param id   The ID of the project to retrieve.
     * @param user The currently authenticated user.
     * @return ApiResponse containing the project or an error message.
     */
    public ApiResponse<ProjectResponseDto> getProjectById(Long id, UserEntity user) {
        try {
            ProjectEntity project = projectRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

            // Check if user has permission to access this project (owns the client or is ADMIN)
            if (user.getRole() != Role.ADMIN && !project.getClient().getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied - You don't own the client for this project");
            }

            ProjectResponseDto projectDto = ProjectResponseDto.fromEntity(project);
            return ApiResponse.success("Project retrieved successfully", projectDto);
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            return ApiResponse.error("Failed to get project by id: " + e.getMessage());
        }
    }

    /**
     * Updates an existing project.
     *
     * @param id      The ID of the project to update.
     * @param request The updated project data.
     * @param user    The currently authenticated user.
     * @return ApiResponse containing the updated project or an error message.
     */
    public ApiResponse<ProjectResponseDto> updateProject(Long id, ProjectRequest request, UserEntity user) {
        try {
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

            // 5. ✅ Check for duplicate project title for the same client (if title is changing)
            if (!project.getTitle().equals(request.getTitle()) &&
                    projectRepository.existsByClient_IdAndTitle(request.getClientId(), request.getTitle())) {
                return ApiResponse.error("Project with title '" + request.getTitle() + "' already exists for this client");
            }

            // 6. Update project fields
            project.setTitle(request.getTitle());
            project.setDescription(request.getDescription());
            project.setStartDate(request.getStartDate());
            project.setEndDate(request.getEndDate());
            project.setStatus(request.getStatus());
            project.setClient(newClient); // Update client association if needed

            ProjectEntity updatedProject = projectRepository.save(project);
            ProjectResponseDto projectDto = ProjectResponseDto.fromEntity(updatedProject);
            return ApiResponse.success("Project updated successfully", projectDto);
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            return ApiResponse.error("Failed to update project: " + e.getMessage());
        }
    }

    /**
     * Retrieves all projects based on user role.
     *
     * @param user The currently authenticated user.
     * @return ApiResponse containing a list of projects or an error message.
     */
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

    /**
     * Retrieves projects for a specific client, enforcing ownership rules.
     *
     * @param clientId The ID of the client whose projects to retrieve.
     * @param user     The currently authenticated user.
     * @return ApiResponse containing a list of projects or an error message.
     */
    public ApiResponse<List<ProjectResponseDto>> getProjectsByClient(Long clientId, UserEntity user) {
        try {
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
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            return ApiResponse.error("Failed to get projects by client: " + e.getMessage());
        }
    }

    /**
     * Deletes an existing project.
     *
     * @param id   The ID of the project to delete.
     * @param user The currently authenticated user.
     * @return ApiResponse confirming deletion or an error message.
     */
    public ApiResponse<String> deleteProject(Long id, UserEntity user) {
        try {
            ProjectEntity project = projectRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

            if (user.getRole() != Role.ADMIN && !project.getClient().getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied - You don't own the client for this project");
            }

            projectRepository.delete(project);
            return ApiResponse.success("Project deleted successfully", null);
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete project: " + e.getMessage());
        }
    }
}