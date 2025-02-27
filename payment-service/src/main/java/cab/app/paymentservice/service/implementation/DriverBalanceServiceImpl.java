package cab.app.paymentservice.service.implementation;

import cab.app.paymentservice.exception.EntityNotFoundException;
import cab.app.paymentservice.exception.InsufficientBalanceException;
import cab.app.paymentservice.model.DriverBalance;
import cab.app.paymentservice.repository.DriverBalanceRepository;
import cab.app.paymentservice.service.DriverBalanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class DriverBalanceServiceImpl implements DriverBalanceService {
    private final DriverBalanceRepository driverBalanceRepository;

    public DriverBalanceServiceImpl(DriverBalanceRepository driverBalanceRepository) {
        this.driverBalanceRepository = driverBalanceRepository;
    }

    @Override
    public void createDriverBalance(Long driverId) {
        if (driverBalanceRepository.findById(driverId).isEmpty()){
            DriverBalance driverBalance = new DriverBalance(driverId, BigDecimal.ZERO);
            driverBalanceRepository.save(driverBalance);
        }
    }

    @Override
    @Transactional
    public void increaseDriverBalance(Long driverId, BigDecimal amount) {
        DriverBalance driverBalance = driverBalanceRepository.findById(driverId).orElseThrow(() -> new EntityNotFoundException("Driver balance not found!"));

        BigDecimal balance = getDriverBalance(driverId);
        driverBalance.setBalance(balance.add(amount));

        driverBalanceRepository.save(driverBalance);
    }

    @Override
    @Transactional
    public BigDecimal getFromDriverBalance(Long driverId, BigDecimal amount) {
        DriverBalance driverBalance = driverBalanceRepository.findById(driverId).orElseThrow(() -> new EntityNotFoundException("Driver balance not found!"));

        BigDecimal balance = getDriverBalance(driverId);
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient money on balance!");
        }

        driverBalance.setBalance(balance.subtract(amount));
        driverBalanceRepository.save(driverBalance);

        return amount;
    }

    @Override
    public BigDecimal getDriverBalance(Long driverId) {
        DriverBalance driverBalance = driverBalanceRepository.findById(driverId).orElseThrow(() -> new EntityNotFoundException("Driver balance not found!"));
        return driverBalance.getBalance();
    }

}
