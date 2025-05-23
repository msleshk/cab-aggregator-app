package cab.app.rideservice.exception.handler;

import cab.app.rideservice.dto.response.exception.ExceptionDto;
import cab.app.rideservice.dto.response.exception.MultiException;
import cab.app.rideservice.exception.*;
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
                MultiException.builder()
                        .message("Validation exception")
                        .errors(errors)
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDto> handleFeignBadRequestException(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DriverAlreadyAssignedException.class)
    public ResponseEntity<ExceptionDto> handleDriverAlreadyAssignedException(DriverAlreadyAssignedException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DriverNotValidException.class)
    public ResponseEntity<ExceptionDto> handleDriverNotValidException(DriverNotValidException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(ExternalServerErrorException.class)
    public ResponseEntity<ExceptionDto> handleExternalServerErrorException(ExternalServerErrorException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ExceptionDto> handleInvalidStatusException(InvalidStatusException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleRideNotFoundException(RideNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }
}
