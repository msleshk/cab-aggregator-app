package cab.app.paymentservice.service;

import java.math.BigDecimal;

public interface DriverBalanceService {
    void createDriverBalance(Long driverId);
    void increaseDriverBalance(Long driverId, BigDecimal amount);

    BigDecimal getFromDriverBalance(Long driverId, BigDecimal amount);

    BigDecimal getDriverBalance(Long driverId);
}
