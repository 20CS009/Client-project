
package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ClientRequest;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ClientService;
import com.example.cpms.dto.ClientResponseDto;
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

    // --- UPDATE RETURN TYPE ---
    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponseDto>> addClient(
            @Valid @RequestBody ClientRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientResponseDto> response = clientService.addClient(request, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- UPDATE RETURN TYPE ---
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDto>> getClientById(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientResponseDto> response = clientService.getClientById(id, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- UPDATE RETURN TYPE ---
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDto>> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientResponseDto> response = clientService.updateClient(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    // --- KEEP RETURN TYPE (LIST OF DTOS) ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponseDto>>> getAllClients(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<List<ClientResponseDto>> response = clientService.getAllClients(currentUser);
        return ResponseEntity.ok(response);
    }

    // --- KEEP RETURN TYPE (STRING IS OK FOR DELETE CONFIRMATION) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<String> response = clientService.deleteClient(id, currentUser);
        return ResponseEntity.ok(response);
    }
}