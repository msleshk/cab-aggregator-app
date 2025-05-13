package com.example.passengerservice.exception.handler;

import com.example.passengerservice.dto.response.exception.ExceptionDto;
import com.example.passengerservice.dto.response.exception.MultiException;
import com.example.passengerservice.exception.EmailAlreadyTakenException;
import com.example.passengerservice.exception.PassengerNotFoundException;
import com.example.passengerservice.exception.PhoneNumberAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ExceptionDto> handlePassengerNotFound(PassengerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionDto.builder()
                .message(ex.getMessage())
                .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MultiException> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MultiException.builder()
                .message("Validation failed!")
                .errors(errors)
                .build()
        );
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<ExceptionDto> handleEmailAlreadyTakenException(EmailAlreadyTakenException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ExceptionDto.builder()
                .message(ex.getMessage())
                .build()
        );
    }

    @ExceptionHandler(PhoneNumberAlreadyTakenException.class)
    public ResponseEntity<ExceptionDto> handlePhoneNumberAlreadyTakenException(PhoneNumberAlreadyTakenException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ExceptionDto.builder()
                .message(ex.getMessage())
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGenericException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionDto.builder()
                .message(ex.getMessage())
                .build()
        );
    }
}
