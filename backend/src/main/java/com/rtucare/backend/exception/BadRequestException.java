package com.rtucare.backend.exception;

import com.rtucare.backend.DTO.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class BadRequestException extends RuntimeException {

    private final ErrorResponseDTO errorResponseDTO;
    public BadRequestException(String message) {
        super(message);
        this.errorResponseDTO = buildDTO(HttpStatus.BAD_REQUEST, message);
    }

    public BadRequestException(String message, HttpStatus httpStatus) {
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
