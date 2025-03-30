package cab.app.paymentservice.controller;

import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.dto.response.ResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "API for managing payments")
@RequestMapping("/api/v1/payment")
public interface PaymentApi {

    @Operation(summary = "Create a new payment", description = "Registers a new payment in the system")
    @PostMapping
    ResponseEntity<Void> createPayment(@RequestBody @Valid CreatePaymentRequest paymentRequest);

    @Operation(summary = "Pay for a ride", description = "Processes payment for a ride")
    @PatchMapping("/pay")
    ResponseEntity<PayResponse> payForRide(@RequestBody @Valid PayRequest payRequest);

    @Operation(summary = "Delete a payment", description = "Removes a payment record by ID")
    @DeleteMapping("/{paymentId}")
    ResponseEntity<Void> deletePayment(@PathVariable("paymentId") Long paymentId);

    @Operation(summary = "Get all payments", description = "Retrieves a paginated list of all payments")
    @GetMapping
    ResponseEntity<ResponseList<PaymentResponse>> getAllPayments(@RequestParam
                                                                 @Min(value = 0, message = "Offset should be greater than 0!")
                                                                 int offset,

                                                                 @RequestParam
                                                                 @Min(value = 1, message = "Limit should be greater than 1")
                                                                 @Max(value = 100, message = "Limit should be less than 100")
                                                                 int limit);

    @Operation(summary = "Get payment by ID", description = "Retrieves payment details by payment ID")
    @GetMapping("/{paymentId}")
    ResponseEntity<PaymentResponse> getPaymentById(@PathVariable("paymentId") Long paymentId);

    @Operation(summary = "Get payment by ride ID", description = "Retrieves payment details by ride ID")
    @GetMapping("/ride/{rideId}")
    ResponseEntity<PaymentResponse> getPaymentByRideId(@PathVariable("rideId") Long rideId);
}
