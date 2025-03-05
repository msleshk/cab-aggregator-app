package cab.app.paymentservice.exception.handler;

import cab.app.paymentservice.dto.response.ExceptionDto;
import cab.app.paymentservice.dto.response.MultiException;
import cab.app.paymentservice.exception.EntityNotFoundException;
import cab.app.paymentservice.exception.InsufficientBalanceException;
import cab.app.paymentservice.exception.PaymentAlreadyExistException;
import cab.app.paymentservice.exception.PromoCodeNotActiveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionDto> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(PaymentAlreadyExistException.class)
    public ResponseEntity<ExceptionDto> handlePaymentAlreadyExistException(PaymentAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionDto.builder()
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(PromoCodeNotActiveException.class)
    public ResponseEntity<ExceptionDto> handlePromoCodeNotActiveException(PromoCodeNotActiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
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
                        .message("Validation failed")
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
