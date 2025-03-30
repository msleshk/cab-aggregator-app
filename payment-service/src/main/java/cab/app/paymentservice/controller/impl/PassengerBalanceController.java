package cab.app.paymentservice.controller.impl;

import cab.app.paymentservice.controller.PassengerBalanceApi;
import cab.app.paymentservice.dto.request.DepositRequest;
import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.service.PassengerBalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passenger-balance")
@RequiredArgsConstructor
public class PassengerBalanceController implements PassengerBalanceApi {

    private final PassengerBalanceService passengerBalanceService;

    @Override
    public ResponseEntity<BalanceResponse> getPassengerBalance(Long passengerId) {
        return ResponseEntity.ok(passengerBalanceService.getPassengerBalance(passengerId));
    }

    @Override
    public ResponseEntity<Void> depositToPassengerBalance(Long passengerId,
                                                          DepositRequest request) {
        passengerBalanceService.increasePassengerBalance(passengerId, request.amount());
        return ResponseEntity.ok().build();
    }
}

