package com.example.driverservice.exception.handler;

import com.example.driverservice.dto.response.exception.ExceptionDto;
import com.example.driverservice.dto.response.exception.MultiException;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MultiException> handleValidationException(MethodArgumentNotValidException ex) {
        HashMap<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                MultiException
                        .builder()
                        .message("Validation failed!")
                        .errors(errors)
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(ResourceAlreadyTakenException.class)
    public ResponseEntity<ExceptionDto> handleResourceAlreadyTakenException(ResourceAlreadyTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }
}
