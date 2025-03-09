package cab.app.paymentservice.controller;

import cab.app.paymentservice.dto.request.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.request.RequestParams;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.dto.response.ResponseList;
import cab.app.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody @Valid CreatePaymentRequest paymentRequest) {
        paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/pay")
    public ResponseEntity<PayResponse> payForRide(@RequestBody @Valid PayRequest payRequest) {
        return ResponseEntity.ok().body(paymentService.payForRide(payRequest));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable("paymentId") Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResponseList<PaymentResponse>> getAllPayments(@RequestBody @Valid RequestParams params) {
        return ResponseEntity.ok().body(paymentService.getAllPayments(params.offset(), params.limit()));
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
