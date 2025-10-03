
package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
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
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientEntity>> addClient(@Valid @RequestBody ClientRequest request, Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientEntity> response = clientService.addClient(request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientEntity>>> getAllClients(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<List<ClientEntity>> response = clientService.getAllClients(currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientEntity>> getClientById(@PathVariable Long id, Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientEntity> response = clientService.getClientById(id, currentUser);
        return ResponseEntity.ok(response);
    }



    /*** Updates an existing client.*/
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientEntity>> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientEntity> response = clientService.updateClient(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    /*** Deletes an existing client.*/
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<String> response = clientService.deleteClient(id, currentUser);
        return ResponseEntity.ok(response);
    }

}
