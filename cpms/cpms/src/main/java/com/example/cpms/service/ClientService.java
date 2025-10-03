package com.example.cpms.service;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ClientRequest;
import com.example.cpms.dto.ClientResponseDto;
import com.example.cpms.dto.DeleteConfirmationDto;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.exception.AccessDeniedException;
import com.example.cpms.exception.ResourceNotFoundException;
import com.example.cpms.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    /**
     * Adds a new client for the current user.
     *
     * @param request The client data from the request body.
     * @param user    The currently authenticated user.
     * @return ApiResponse containing the saved client or an error message.
     */
    @Transactional
    public ApiResponse<ClientResponseDto> addClient(ClientRequest request, UserEntity user) {
        try {
            ClientEntity client = new ClientEntity();
            client.setName(request.getName());
            client.setEmail(request.getEmail());
            client.setPhone(request.getPhone());
            client.setCompanyName(request.getCompanyName());
            client.setUser(user);

            ClientEntity savedClient = clientRepository.save(client);


            ClientResponseDto clientDto = modelMapper.map(savedClient, ClientResponseDto.class);

            return ApiResponse.success("Client added successfully", clientDto);
        } catch (Exception e) {
            return ApiResponse.error("Failed to add client: " + e.getMessage());
        }
    }

    /**
     * Retrieves all clients based on user role.
     * Admin sees all clients, regular users see only theirs.
     *
     * @param user The currently authenticated user.
     * @return ApiResponse containing a list of clients or an error message.
     */
    public ApiResponse<List<ClientResponseDto>> getAllClients(UserEntity user) {
        try {
            List<ClientEntity> clients;
            if (user.getRole() == Role.ADMIN) {
                clients = clientRepository.findAll();
            } else {
                clients = clientRepository.findByUser(user);
            }


            List<ClientResponseDto> clientDtos = clients.stream()
                    .map(client -> modelMapper.map(client, ClientResponseDto.class))
                    .collect(Collectors.toList());

            return ApiResponse.success("Clients retrieved successfully", clientDtos);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get all clients: " + e.getMessage());
        }
    }

    /**
     * Retrieves a specific client by ID, enforcing ownership rules.
     *
     * @param id   The ID of the client to retrieve.
     * @param user The currently authenticated user.
     * @return ApiResponse containing the client or an error message.
     * @throws ResourceNotFoundException if the client does not exist.
     * @throws AccessDeniedException      if the user lacks permission to access the client.
     */
    public ApiResponse<ClientResponseDto> getClientById(Long id, UserEntity user) {
        try {
            // 1. Find client by ID
            ClientEntity client = clientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

            // 2. Check ownership/access rights
            if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied - You don't have permission to access this client");
            }

            // 3. Convert Entity to DTO
            ClientResponseDto clientDto = modelMapper.map(client, ClientResponseDto.class);

            // 4. Return success response
            return ApiResponse.success("Client retrieved successfully", clientDto);
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            // Catch unexpected errors
            return ApiResponse.error("Failed to get client by id: " + e.getMessage());
        }
    }

    /**
     * Updates an existing client.
     *
     * @param id      The ID of the client to update.
     * @param request The updated client data.
     * @param user    The currently authenticated user.
     * @return ApiResponse containing the updated client or an error message.
     * @throws ResourceNotFoundException if the client does not exist.
     * @throws AccessDeniedException      if the user lacks permission.
     */
    public ApiResponse<ClientResponseDto> updateClient(Long id, ClientRequest request, UserEntity user) {
        try {
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
            // Note: user association typically shouldn't change. If it needs to, add logic here.

            // 4. Save and convert to DTO
            ClientEntity updatedClient = clientRepository.save(client);
            ClientResponseDto clientDto = modelMapper.map(updatedClient, ClientResponseDto.class);

            // 5. Return success response
            return ApiResponse.success("Client updated successfully", clientDto);
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            // Catch unexpected errors
            return ApiResponse.error("Failed to update client: " + e.getMessage());
        }
    }

    /**
     * Deletes an existing client.
     *
     * @param id   The ID of the client to delete.
     * @param user The currently authenticated user.
     * @return ApiResponse confirming deletion or an error message.
     * @throws ResourceNotFoundException if the client does not exist.
     * @throws AccessDeniedException      if the user lacks permission.
     */
    public ApiResponse<DeleteConfirmationDto> deleteClient(Long id, UserEntity user) {
        try {
            // 1. Find the existing client
            ClientEntity client = clientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

            // 2. Check ownership/access rights
            if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Access denied - You don't have permission to delete this client");
            }

            // 3. Delete the client
            clientRepository.delete(client);

            // 4. Return success response with structured DTO
            return ApiResponse.success("Client deleted successfully", new DeleteConfirmationDto("Client deleted successfully"));
        } catch (ResourceNotFoundException | AccessDeniedException e) {
            // Re-throw custom exceptions so GlobalExceptionHandler can catch them
            throw e;
        } catch (Exception e) {
            // Catch unexpected errors
            return ApiResponse.error("Failed to delete client: " + e.getMessage());
        }
    }
}