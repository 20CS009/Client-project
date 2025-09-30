// File: src/main/java/com/example/cpms/service/ClientService.java
package com.example.cpms.service;

import com.example.cpms.dto.ClientRequest;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.Role;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientEntity addClient(ClientRequest request, UserEntity user) {
        ClientEntity client = new ClientEntity();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setCompanyName(request.getCompanyName());
        client.setUser(user);
        return clientRepository.save(client);
    }

    public List<ClientEntity> getAllClients(UserEntity user) {
        if (user.getRole() == Role.ADMIN) {
            return clientRepository.findAll();
        } else {
            return clientRepository.findByUser(user);
        }
    }

    public ClientEntity getClientById(Long id, UserEntity user) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (user.getRole() != Role.ADMIN && !client.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        return client;
    }
}