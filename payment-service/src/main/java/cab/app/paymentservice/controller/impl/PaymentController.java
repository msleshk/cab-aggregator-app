package cab.app.paymentservice.controller.impl;

import cab.app.paymentservice.controller.PaymentApi;
import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.dto.response.ResponseList;
import cab.app.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    @Override
    public ResponseEntity<Void> createPayment(CreatePaymentRequest paymentRequest) {
        paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<PayResponse> payForRide(PayRequest payRequest) {
        return ResponseEntity.ok().body(paymentService.payForRide(payRequest));
    }

    @Override
    public ResponseEntity<Void> deletePayment(Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ResponseList<PaymentResponse>> getAllPayments(int offset, int limit) {
        return ResponseEntity.ok().body(paymentService.getAllPayments(offset, limit));
    }

    @Override
    public ResponseEntity<PaymentResponse> getPaymentById(Long paymentId) {
        return ResponseEntity.ok().body(paymentService.getPaymentById(paymentId));
    }

    @Override
    public ResponseEntity<PaymentResponse> getPaymentByRideId(Long rideId) {
        return ResponseEntity.ok().body(paymentService.getPaymentByRide(rideId));
    }
}

