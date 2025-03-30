package cab.app.paymentservice.controller;


import cab.app.paymentservice.dto.response.BalanceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Driver Balance", description = "API for managing driver balances")
@RequestMapping("/api/v1/driver-balance")
public interface DriverBalanceApi {

    @Operation(summary = "Get driver balance", description = "Retrieves the balance of a specific driver by ID")
    @GetMapping("/{id}")
    ResponseEntity<BalanceResponse> getDriverBalance(@PathVariable("id") Long driverId);
}
