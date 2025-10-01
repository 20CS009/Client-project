package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ClientRequest;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")

public class ClientController {

    private final ClientService clientService;

    // Constructor injection
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClientEntity>> addClient(@Valid @RequestBody ClientRequest request, Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ClientEntity savedClient = clientService.addClient(request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Client added successfully", savedClient));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientEntity>>> getAllClients(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        List<ClientEntity> clients = clientService.getAllClients(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Clients retrieved successfully", clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientEntity>> getClientById(@PathVariable Long id, Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ClientEntity client = clientService.getClientById(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Client retrieved successfully", client));
    }
}