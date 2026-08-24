package com.rtucare.backend.exception;

import com.rtucare.backend.DTO.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResourceNotFoundException extends RuntimeException {

    private final ErrorResponseDTO errorResponseDTO;

    public ResourceNotFoundException(String message) {
        super(message);
        this.errorResponseDTO = buildDTO(HttpStatus.NOT_FOUND, message);
    }

    public ResourceNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.errorResponseDTO = buildDTO(httpStatus, message);
    }

    public ErrorResponseDTO getErrorResponseDTO() {
        return errorResponseDTO;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(errorResponseDTO.getStatus());
    }

    private ErrorResponseDTO buildDTO(HttpStatus httpStatus, String message) {
        ErrorResponseDTO dto = new ErrorResponseDTO();
        dto.setStatus(httpStatus.value());
        dto.setError(httpStatus.getReasonPhrase());
        dto.setMessage(message);
        dto.setTimestamp(LocalDateTime.now());
        return dto;
    }
}
