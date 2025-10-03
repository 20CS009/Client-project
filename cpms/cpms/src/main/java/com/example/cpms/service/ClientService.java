// File: src/main/java/com/example/cpms/service/ClientService.java
package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ClientRequest;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.dto.ClientResponseDto; // Add this import
import java.util.stream.Collectors;
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

    // --- UPDATE RETURN TYPE ---
    public ApiResponse<ClientResponseDto> addClient(ClientRequest request, UserEntity user) {
        try {
            ClientEntity client = new ClientEntity();
            client.setName(request.getName());
            client.setEmail(request.getEmail());
            client.setPhone(request.getPhone());
            client.setCompanyName(request.getCompanyName());
            client.setUser(user);

            ClientEntity savedClient = clientRepository.save(client);
            // --- CONVERT ENTITY TO DTO ---
            ClientResponseDto clientDto = ClientResponseDto.fromEntity(savedClient);
            return ApiResponse.success("Client added successfully", clientDto);
        } catch (Exception e) {
            // Keep error handling for unexpected issues
            return ApiResponse.error("Failed to add client: " + e.getMessage());
        }
    }

    // --- UPDATE RETURN TYPE ---
    public ApiResponse<ClientResponseDto> getClientById(Long id, UserEntity user) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't have permission to access this client");
        }
        // --- CONVERT ENTITY TO DTO ---
        ClientResponseDto clientDto = ClientResponseDto.fromEntity(client);
        return ApiResponse.success("Client retrieved successfully", clientDto);
    }

    // --- UPDATE RETURN TYPE ---
    public ApiResponse<ClientResponseDto> updateClient(Long id, ClientRequest request, UserEntity user) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't have permission to update this client");
        }

        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setCompanyName(request.getCompanyName());

        ClientEntity updatedClient = clientRepository.save(client);
        // --- CONVERT ENTITY TO DTO ---
        ClientResponseDto clientDto = ClientResponseDto.fromEntity(updatedClient);
        return ApiResponse.success("Client updated successfully", clientDto);
    }

    // --- KEEP RETURN TYPE AS IS (LIST OF DTOS) ---
    public ApiResponse<List<ClientResponseDto>> getAllClients(UserEntity user) {
        try {
            List<ClientEntity> clients;
            if (user.getRole() == Role.ADMIN) {
                clients = clientRepository.findAll();
            } else {
                clients = clientRepository.findByUser(user);
            }
            // --- CONVERT LIST OF ENTITIES TO LIST OF DTOS ---
            List<ClientResponseDto> clientDtos = clients.stream()
                    .map(ClientResponseDto::fromEntity)
                    .toList();
            return ApiResponse.success("Clients retrieved successfully", clientDtos);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get all clients: " + e.getMessage());
        }
    }

    // --- UPDATE RETURN TYPE (STRING RESPONSE IS FINE FOR DELETE) ---
    public ApiResponse<String> deleteClient(Long id, UserEntity user) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied - You don't have permission to delete this client");
        }

        clientRepository.delete(client);

        // --- STRING MESSAGE IS ACCEPTABLE FOR DELETE ---
        return ApiResponse.success("Client deleted successfully", null);
        // OR if you want to send an object:
        // return ApiResponse.success("Client deleted successfully", new DeleteConfirmationDto("Client " + id + " deleted"));
    }
}

// Optional: A simple DTO for delete confirmation if needed
// class DeleteConfirmationDto {
//     private String message;
//     public DeleteConfirmationDto(String message) { this.message = message; }
//     public String getMessage() { return message; }
// }