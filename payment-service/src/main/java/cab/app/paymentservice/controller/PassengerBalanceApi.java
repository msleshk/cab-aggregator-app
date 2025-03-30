package cab.app.paymentservice.controller;

import cab.app.paymentservice.dto.request.DepositRequest;
import cab.app.paymentservice.dto.response.BalanceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Passenger Balance", description = "API for managing passenger balances")
@RequestMapping("/api/v1/passenger-balance")
public interface PassengerBalanceApi {

    @Operation(summary = "Get passenger balance", description = "Retrieves the balance of a specific passenger by ID")
    @GetMapping("/{id}")
    ResponseEntity<BalanceResponse> getPassengerBalance(@PathVariable("id") Long passengerId);

    @Operation(summary = "Deposit to passenger balance", description = "Increases the balance of a passenger by a given amount")
    @PatchMapping("/{id}")
    ResponseEntity<Void> depositToPassengerBalance(@PathVariable("id") Long passengerId,
                                                   @RequestBody DepositRequest request);
}
