package cab.app.ratingservice.exception.handler;

import cab.app.ratingservice.dto.response.exception.ExceptionDto;
import cab.app.ratingservice.dto.response.exception.MultiException;
import cab.app.ratingservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RatingAlreadyExistException.class)
    public ResponseEntity<ExceptionDto> handleRatingAlreadyExistException(RatingAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleRatingNotFoundException(RatingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(RideNotCompletedException.class)
    public ResponseEntity<ExceptionDto> handleRideNotCompletedException(RideNotCompletedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
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

    @ExceptionHandler(ExternalServerErrorException.class)
    public ResponseEntity<ExceptionDto> handleExternalServerErrorException(ExternalServerErrorException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MultiException> handleValidationException(MethodArgumentNotValidException ex) {
        HashMap<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                MultiException.builder()
                        .message("Validation failed!")
                        .errors(errors)
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
