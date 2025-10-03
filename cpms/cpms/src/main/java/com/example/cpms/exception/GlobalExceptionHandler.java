package com.example.cpms.exception;

import com.example.cpms.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to provide consistent error responses across the API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid annotations on request DTOs.
     * e.g., @NotBlank, @Email violations.
     *
     * @param ex The MethodArgumentNotValidException containing validation errors.
     * @return ResponseEntity with a map of field names and error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        // Returns 400 Bad Request with field-specific errors
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles our custom ResourceNotFoundException.
     * Maps to HTTP 404 Not Found.
     *
     * @param ex The ResourceNotFoundException that was thrown.
     * @return ResponseEntity with an ApiResponse error structure.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        // Returns 404 Not Found with a structured error message
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles our custom AccessDeniedException.
     * Maps to HTTP 403 Forbidden.
     *
     * @param ex The AccessDeniedException that was thrown.
     * @return ResponseEntity with an ApiResponse error structure.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        // Returns 403 Forbidden with a structured error message
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(ResourceException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}