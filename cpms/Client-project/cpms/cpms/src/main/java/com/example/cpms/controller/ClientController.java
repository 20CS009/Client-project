//package com.example.cpms.controller;
//
//import com.example.cpms.dto.ApiResponse;
//import com.example.cpms.dto.ClientRequest;
//import com.example.cpms.dto.ClientResponseDto;
//import com.example.cpms.dto.DeleteConfirmationDto;
//import com.example.cpms.entity.ClientEntity;
//import com.example.cpms.entity.UserEntity;
//import com.example.cpms.service.ClientService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/clients")
//public class ClientController {
//
//    @Autowired
//    private ClientService clientService;
//
//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse<ClientResponseDto>> addClient(
//            @Valid @RequestBody ClientRequest request,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<ClientResponseDto> response = clientService.addClient(request, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<ClientResponseDto>> getClientById(
//            @PathVariable Long id,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<ClientResponseDto> response = clientService.getClientById(id, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<ClientResponseDto>> updateClient(
//            @PathVariable Long id,
//            @Valid @RequestBody ClientRequest request,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<ClientResponseDto> response = clientService.updateClient(id, request, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ClientResponseDto>>> getAllClients(Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<List<ClientResponseDto>> response = clientService.getAllClients(currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<DeleteConfirmationDto>> deleteClient(
//            @PathVariable Long id,
//            Authentication authentication) {
//        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
//        ApiResponse<DeleteConfirmationDto> response = clientService.deleteClient(id, currentUser);
//        return ResponseEntity.ok(response);
//    }
//}

package com.example.cpms.controller;

import com.example.cpms.dto.ApiResponse;
import com.example.cpms.dto.ClientRequest;
import com.example.cpms.dto.ClientResponseDto;
import com.example.cpms.entity.ClientEntity;
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponseDto>> addClient(
            @Valid @RequestBody ClientRequest request,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            ClientEntity savedClient = clientService.addClient(request, currentUser);
            ClientResponseDto clientDto = modelMapper.map(savedClient, ClientResponseDto.class);
            return ResponseEntity.ok(ApiResponse.success("Client added successfully", clientDto));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponseDto>>> getAllClients(Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            List<ClientEntity> clients = clientService.getAllClients(currentUser);
            List<ClientResponseDto> clientDtos = clients.stream()
                    .map(client -> modelMapper.map(client, ClientResponseDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Clients retrieved successfully", clientDtos));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDto>> getClientById(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            ClientEntity client = clientService.getClientById(id, currentUser);
            ClientResponseDto clientDto = modelMapper.map(client, ClientResponseDto.class);
            return ResponseEntity.ok(ApiResponse.success("Client retrieved successfully", clientDto));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientResponseDto>> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest request,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            ClientEntity updatedClient = clientService.updateClient(id, request, currentUser);
            ClientResponseDto clientDto = modelMapper.map(updatedClient, ClientResponseDto.class);
            return ResponseEntity.ok(ApiResponse.success("Client updated successfully", clientDto));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            clientService.deleteClient(id, currentUser);
            return ResponseEntity.ok(ApiResponse.success("Client deleted successfully", null));
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler catch custom exceptions
        }
    }
}