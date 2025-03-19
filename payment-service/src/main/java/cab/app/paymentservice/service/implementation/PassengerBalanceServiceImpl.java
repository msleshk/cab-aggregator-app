package cab.app.paymentservice.service.implementation;

import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.exception.EntityNotFoundException;
import cab.app.paymentservice.exception.InsufficientBalanceException;
import cab.app.paymentservice.model.PassengerBalance;
import cab.app.paymentservice.repository.PassengerBalanceRepository;
import cab.app.paymentservice.service.PassengerBalanceService;
import cab.app.paymentservice.util.BalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PassengerBalanceServiceImpl implements PassengerBalanceService {
    private final PassengerBalanceRepository passengerBalanceRepository;
    private final BalanceMapper mapper;

    @Override
    @Transactional
    public void createPassengerBalance(Long passengerId) {
        if (!passengerBalanceRepository.existsById(passengerId)) {
            PassengerBalance passengerBalance = new PassengerBalance(passengerId, BigDecimal.ZERO);
            passengerBalanceRepository.save(passengerBalance);
        }
    }

    @Override
    @Transactional
    public void increasePassengerBalance(Long passengerId, BigDecimal amount) {
        PassengerBalance passengerBalance = findPassengerBalance(passengerId);

        BigDecimal currentBalance = passengerBalance.getBalance();
        passengerBalance.setBalance(currentBalance.add(amount));

        passengerBalanceRepository.save(passengerBalance);
    }

    @Override
    @Transactional
    public void withdrawFromPassengerBalance(Long passengerId, BigDecimal amount) {
        PassengerBalance passengerBalance = findPassengerBalance(passengerId);

        BigDecimal currentBalance = passengerBalance.getBalance();
        checkIfBalanceSufficient(currentBalance, amount);

        passengerBalance.setBalance(currentBalance.subtract(amount));
        passengerBalanceRepository.save(passengerBalance);
    }

    @Override
    public BalanceResponse getPassengerBalance(Long passengerId) {
        return mapper.toDto(findPassengerBalance(passengerId));
    }

    private PassengerBalance findPassengerBalance(Long passengerId) {
        return passengerBalanceRepository.findById(passengerId).orElseThrow(() -> new EntityNotFoundException("Passenger balance not found!"));
    }

    private void checkIfBalanceSufficient(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient money on balance!");
        }
    }
}
