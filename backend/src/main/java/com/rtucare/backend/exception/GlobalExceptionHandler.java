package com.rtucare.backend.exception;

import com.rtucare.backend.DTO.response.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex,
                                                                   WebRequest request) {
        logger.error("Resource not found: {}", ex.getMessage());
        ErrorResponseDTO dto = ex.getErrorResponseDTO();
        dto.setPath(getPath(request));
        return ResponseEntity.status(dto.getStatus()).body(dto);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResource(DuplicateResourceException ex,
                                                                     WebRequest request) {
        logger.error("Duplicate resource: {}", ex.getMessage());
        ErrorResponseDTO dto = ex.getErrorResponseDTO();
        dto.setPath(getPath(request));
        return ResponseEntity.status(dto.getStatus()).body(dto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(InvalidCredentialsException ex,
                                                                      WebRequest request) {
        logger.error("Invalid credentials: {}", ex.getMessage());
        ErrorResponseDTO dto = ex.getErrorResponseDTO();
        dto.setPath(getPath(request));
        return ResponseEntity.status(dto.getStatus()).body(dto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(BadRequestException ex,
                                                              WebRequest request) {
        logger.error("Bad request: {}", ex.getMessage());
        ErrorResponseDTO dto = ex.getErrorResponseDTO();
        dto.setPath(getPath(request));
        return ResponseEntity.status(dto.getStatus()).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex,
                                                              WebRequest request) {
        logger.error("Validation failed: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponseDTO dto = new ErrorResponseDTO();
        dto.setStatus(HttpStatus.BAD_REQUEST.value());
        dto.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        dto.setMessage("Validation failed");
        dto.setTimestamp(LocalDateTime.now());
        dto.setPath(getPath(request));
        dto.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthentication(AuthenticationException ex,
                                                                  WebRequest request) {
        logger.error("Authentication failed: {}", ex.getMessage());

        ErrorResponseDTO dto = new ErrorResponseDTO();
        dto.setStatus(HttpStatus.UNAUTHORIZED.value());
        dto.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        dto.setMessage("Authentication failed: " + ex.getMessage());
        dto.setTimestamp(LocalDateTime.now());
        dto.setPath(getPath(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred", ex);

        ErrorResponseDTO dto = new ErrorResponseDTO();
        dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        dto.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        dto.setMessage("An unexpected error occurred. Please try again later.");
        dto.setTimestamp(LocalDateTime.now());
        dto.setPath(getPath(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    private String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return "";
    }
}