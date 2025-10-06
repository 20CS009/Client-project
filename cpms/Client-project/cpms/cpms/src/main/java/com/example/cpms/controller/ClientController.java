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
import com.example.cpms.dto.ClientResponseDto; // ✅ Import DTO
import com.example.cpms.dto.DeleteConfirmationDto; // ✅ Import DTO
import com.example.cpms.entity.UserEntity;
import com.example.cpms.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    //  Change return type from ClientEntity to ClientResponseDto
    public ResponseEntity<ApiResponse<ClientResponseDto>> addClient(
            @Valid @RequestBody ClientRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientResponseDto> response = clientService.addClient(request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    // Change return type from List<ClientEntity> to List<ClientResponseDto>
    public ResponseEntity<ApiResponse<List<ClientResponseDto>>> getAllClients(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<List<ClientResponseDto>> response = clientService.getAllClients(currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    // Change return type from ClientEntity to ClientResponseDto
    public ResponseEntity<ApiResponse<ClientResponseDto>> getClientById(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientResponseDto> response = clientService.getClientById(id, currentUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    // Change return type from ClientEntity to ClientResponseDto
    public ResponseEntity<ApiResponse<ClientResponseDto>> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest request,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<ClientResponseDto> response = clientService.updateClient(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    // Change return type from String to DeleteConfirmationDto
    public ResponseEntity<ApiResponse<DeleteConfirmationDto>> deleteClient(
            @PathVariable Long id,
            Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        ApiResponse<DeleteConfirmationDto> response = clientService.deleteClient(id, currentUser);
        return ResponseEntity.ok(response);
    }

}
