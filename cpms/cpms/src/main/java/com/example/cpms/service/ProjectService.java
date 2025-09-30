package com.example.cpms.service;

import com.example.cpms.dto.ProjectRequest;
import com.example.cpms.entity.*;
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

    public ProjectEntity addProject(ProjectRequest request, UserEntity user) {
        // 1. Find client
        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // 2. Check ownership (if user is not ADMIN)
        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't own this client");
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
    }

    public List<ProjectEntity> getProjectsByClient(Long clientId, UserEntity user) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return projectRepository.findByClient_Id(clientId);
    }

    public List<ProjectEntity> getAllProjects(UserEntity user) {
        if (user.getRole() == Role.ADMIN) {
            return projectRepository.findAll();
        } else {
            return projectRepository.findByUser(user);
        }
    }
}