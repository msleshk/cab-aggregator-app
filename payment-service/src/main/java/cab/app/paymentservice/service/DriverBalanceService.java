package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.response.BalanceResponse;

import java.math.BigDecimal;

public interface DriverBalanceService {
    void createDriverBalance(Long driverId);

    void increaseDriverBalance(Long driverId, BigDecimal amount);

    BigDecimal getFromDriverBalance(Long driverId, BigDecimal amount);

    BalanceResponse getDriverBalance(Long driverId);
}
