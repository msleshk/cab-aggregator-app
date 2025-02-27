package cab.app.paymentservice.controller;

import cab.app.paymentservice.dto.request.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.exception.ValidationException;
import cab.app.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody @Valid CreatePaymentRequest paymentRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/pay")
    public ResponseEntity<PayResponse> payForRide(@RequestBody @Valid PayRequest payRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        return ResponseEntity.ok().body(paymentService.payForRide(payRequest));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable("paymentId") Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok().body(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable("paymentId") Long paymentId) {
        return ResponseEntity.ok().body(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<PaymentResponse> getPaymentByRideId(@PathVariable("rideId") Long rideId) {
        return ResponseEntity.ok().body(paymentService.getPaymentByRide(rideId));
    }
}
