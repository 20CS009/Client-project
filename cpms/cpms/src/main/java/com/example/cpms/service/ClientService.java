// File: src/main/java/com/example/cpms/service/ClientService.java
package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ClientRequest;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.exception.AccessDeniedException;
import com.example.cpms.exception.ResourceNotFoundException;
import com.example.cpms.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ApiResponse<ClientEntity> addClient(ClientRequest request, UserEntity user) {
        try {
            ClientEntity client = new ClientEntity();
            client.setName(request.getName());
            client.setEmail(request.getEmail());
            client.setPhone(request.getPhone());
            client.setCompanyName(request.getCompanyName());
            client.setUser(user);

            ClientEntity savedClient = clientRepository.save(client);
            return ApiResponse.success("Client added successfully", savedClient);
        } catch (Exception e) {
            return ApiResponse.error("Failed to add client: " + e.getMessage());
        }
    }

    public ApiResponse<List<ClientEntity>> getAllClients(UserEntity user) {
        try {
            List<ClientEntity> clients;
            if (user.getRole() == Role.ADMIN) {
                clients = clientRepository.findAll();
            } else {
                clients = clientRepository.findByUser(user);
            }
            return ApiResponse.success("Clients retrieved successfully", clients);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get all clients: " + e.getMessage());
        }
    }

    public ApiResponse<ClientEntity> getClientById(Long id, UserEntity user) {
        // Use custom exception for not found
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        // Use custom exception for access denied
        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't have permission to access this client");
        }
        return ApiResponse.success("Client retrieved successfully", client);
    }

    // --- NEW CRUD METHODS ---

    /**
     * Updates an existing client.
     */
    public ApiResponse<ClientEntity> updateClient(Long id, ClientRequest request, UserEntity user) {
        // 1. Find the existing client
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        // 2. Check ownership/access rights
        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't have permission to update this client");
        }

        // 3. Update fields
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setCompanyName(request.getCompanyName());

        // 4. Save and return success response
        ClientEntity updatedClient = clientRepository.save(client);
        return ApiResponse.success("Client updated successfully", updatedClient);
    }

    /**
     * Deletes an existing client.
     */
    public ApiResponse<String> deleteClient(Long id, UserEntity user) {
        // 1. Find the existing client
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        // 2. Check ownership/access rights
        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't have permission to delete this client");
        }

        // 3. Delete the client
        clientRepository.delete(client);

        // 4. Return success response
        return ApiResponse.success("Client deleted successfully", null);
    }
}