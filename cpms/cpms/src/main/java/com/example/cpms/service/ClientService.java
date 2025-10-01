package com.example.cpms.service;

import com.example.cpms.dto.ClientRequest;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientEntity addClient(ClientRequest request, UserEntity user) {
        try {
            ClientEntity client = new ClientEntity();
            client.setName(request.getName());
            client.setEmail(request.getEmail());
            client.setPhone(request.getPhone());
            client.setCompanyName(request.getCompanyName());
            client.setUser(user);
            return clientRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add client: " + e.getMessage());
        }
    }

    public List<ClientEntity> getAllClients(UserEntity user) {
        try {
            if (user.getRole() == Role.ADMIN) {
                return clientRepository.findAll();
            } else {
                return clientRepository.findByUser(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all clients: " + e.getMessage());
        }
    }

    public ClientEntity getClientById(Long id, UserEntity user) {
        try {
            ClientEntity client = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

            if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Access denied - You don't have permission to access this client");
            }
            return client;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get client by id: " + e.getMessage());
        }
    }
}