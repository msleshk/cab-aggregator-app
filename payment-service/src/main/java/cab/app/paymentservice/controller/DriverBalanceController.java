package cab.app.paymentservice.controller;

import cab.app.paymentservice.service.DriverBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/driver-balance")
public class DriverBalanceController {
    private final DriverBalanceService driverBalanceService;

    public DriverBalanceController(DriverBalanceService driverBalanceService) {
        this.driverBalanceService = driverBalanceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BigDecimal> getDriverBalance(@PathVariable("id") Long driverId) {
        return ResponseEntity.ok().body(driverBalanceService.getDriverBalance(driverId));
    }
}
