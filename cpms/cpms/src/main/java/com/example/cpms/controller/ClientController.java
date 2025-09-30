package com.example.cpms.controller;

import com.example.cpms.dto.ClientRequest;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientEntity> addClient(@Valid @RequestBody ClientRequest request, Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ClientEntity savedClient = clientService.addClient(request, currentUser);
        return ResponseEntity.ok(savedClient);
    }

    @GetMapping
    public ResponseEntity<List<ClientEntity>> getAllClients(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        List<ClientEntity> clients = clientService.getAllClients(currentUser);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long id, Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ClientEntity client = clientService.getClientById(id, currentUser);
        return ResponseEntity.ok(client);
    }
}