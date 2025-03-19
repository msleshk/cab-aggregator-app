package cab.app.paymentservice.controller.impl;

import cab.app.paymentservice.controller.DriverBalanceApi;
import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.service.DriverBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/driver-balance")
@RequiredArgsConstructor
public class DriverBalanceController implements DriverBalanceApi {

    private final DriverBalanceService driverBalanceService;

    @Override
    public ResponseEntity<BalanceResponse> getDriverBalance(Long driverId) {
        return ResponseEntity.ok(driverBalanceService.getDriverBalance(driverId));
    }
}

