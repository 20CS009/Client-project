package com.example.cpms.service;

import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.entity.*;
import com.example.cpms.repository.ClientRepository;
import com.example.cpms.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;

    public ProjectEntity addProject(ProjectRequest request, UserEntity user) {
        try {
            // 1. Find client
            ClientEntity client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

            // 2. Check ownership (if user is not ADMIN)
            if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("You don't own this client with id: " + request.getClientId());
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

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add project: " + e.getMessage());
        }
    }

    public List<ProjectEntity> getProjectsByClient(Long clientId, UserEntity user) {
        try {
            ClientEntity client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

            if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Access denied - You don't have permission to access projects for this client");
            }

            return projectRepository.findByClient_Id(clientId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get projects by client: " + e.getMessage());
        }
    }

    public List<ProjectEntity> getAllProjects(UserEntity user) {
        try {
            if (user.getRole() == Role.ADMIN) {
                return projectRepository.findAll();
            } else {
                return projectRepository.findByUser(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all projects: " + e.getMessage());
        }
    }
}