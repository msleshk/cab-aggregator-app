package cab.app.paymentservice.service.implementation;

import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.exception.EntityNotFoundException;
import cab.app.paymentservice.exception.InsufficientBalanceException;
import cab.app.paymentservice.model.DriverBalance;
import cab.app.paymentservice.repository.DriverBalanceRepository;
import cab.app.paymentservice.service.DriverBalanceService;
import cab.app.paymentservice.util.BalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DriverBalanceServiceImpl implements DriverBalanceService {

    private final DriverBalanceRepository driverBalanceRepository;
    private final BalanceMapper mapper;

    @Override
    @Transactional
    public void createDriverBalance(Long driverId) {
        if (driverBalanceRepository.findById(driverId).isEmpty()) {
            DriverBalance driverBalance = new DriverBalance(driverId, BigDecimal.ZERO);
            driverBalanceRepository.save(driverBalance);
        }
    }

    @Override
    @Transactional
    public void increaseDriverBalance(Long driverId, BigDecimal amount) {
        DriverBalance driverBalance = findDriverBalance(driverId);

        BigDecimal balance = driverBalance.getBalance();
        driverBalance.setBalance(balance.add(amount));

        driverBalanceRepository.save(driverBalance);
    }

    @Override
    @Transactional
    public BigDecimal getFromDriverBalance(Long driverId, BigDecimal amount) {
        DriverBalance driverBalance = findDriverBalance(driverId);

        BigDecimal balance = driverBalance.getBalance();
        checkIfBalanceSufficient(balance, amount);

        driverBalance.setBalance(balance.subtract(amount));
        driverBalanceRepository.save(driverBalance);

        return amount;
    }

    @Override
    public BalanceResponse getDriverBalance(Long driverId) {
        return mapper.toDto(findDriverBalance(driverId));
    }

    private DriverBalance findDriverBalance(Long driverId) {
        return driverBalanceRepository.findById(driverId).orElseThrow(() -> new EntityNotFoundException("Driver balance not found!"));
    }

    private void checkIfBalanceSufficient(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient money on balance!");
        }
    }

}
