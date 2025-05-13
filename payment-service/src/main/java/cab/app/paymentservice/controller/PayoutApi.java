package cab.app.paymentservice.controller;

import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payouts", description = "API for managing driver payouts")
@RequestMapping("/api/v1/payout")
public interface PayoutApi {

    @Operation(summary = "Create a payout", description = "Processes a payout request for a driver")
    @PostMapping
    ResponseEntity<PayoutResponse> getPayout(@RequestBody @Valid PayoutRequest payoutRequest);
}
