package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.response.BalanceResponse;

import java.math.BigDecimal;

public interface PassengerBalanceService {
    void createPassengerBalance(Long passengerId);

    void increasePassengerBalance(Long passengerId, BigDecimal amount);

    void withdrawFromPassengerBalance(Long passengerId, BigDecimal amount);

    BalanceResponse getPassengerBalance(Long passengerId);
}
