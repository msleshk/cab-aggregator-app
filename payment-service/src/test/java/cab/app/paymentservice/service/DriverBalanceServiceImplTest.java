package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.exception.EntityNotFoundException;
import cab.app.paymentservice.exception.InsufficientBalanceException;
import cab.app.paymentservice.model.DriverBalance;
import cab.app.paymentservice.repository.DriverBalanceRepository;
import cab.app.paymentservice.service.implementation.DriverBalanceServiceImpl;
import cab.app.paymentservice.util.BalanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static cab.app.paymentservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverBalanceServiceImplTest {

    @Mock
    private DriverBalanceRepository driverBalanceRepository;
    @Mock
    private BalanceMapper balanceMapper;

    @InjectMocks
    private DriverBalanceServiceImpl driverBalanceService;

    private DriverBalance driverBalance;
    private BalanceResponse balanceResponse;

    @BeforeEach
    void setUp() {
        driverBalance = new DriverBalance(DRIVER_ID, INITIAL_BALANCE);
        balanceResponse = new BalanceResponse(DRIVER_ID, INITIAL_BALANCE);
    }

    @Test
    void shouldCreateDriverBalanceWhenNotExists() {
        when(driverBalanceRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        driverBalanceService.createDriverBalance(DRIVER_ID);

        verify(driverBalanceRepository, times(1)).save(any(DriverBalance.class));
    }

    @Test
    void shouldNotCreateDriverBalanceWhenAlreadyExists() {
        when(driverBalanceRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driverBalance));

        driverBalanceService.createDriverBalance(DRIVER_ID);

        verify(driverBalanceRepository, never()).save(any(DriverBalance.class));
    }

    @Test
    void shouldIncreaseDriverBalance() {
        when(driverBalanceRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driverBalance));

        driverBalanceService.increaseDriverBalance(DRIVER_ID, BALANCE_AMOUNT);

        assertEquals(BALANCE_AMOUNT, driverBalance.getBalance());
        verify(driverBalanceRepository, times(1)).save(driverBalance);
    }

    @Test
    void shouldThrowExceptionWhenGettingBalanceForNonExistentDriver() {
        when(driverBalanceRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                driverBalanceService.getDriverBalance(DRIVER_ID));

        assertEquals(EXCEPTION_BALANCE_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldGetDriverBalance() {
        when(driverBalanceRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driverBalance));
        when(balanceMapper.toDto(driverBalance)).thenReturn(balanceResponse);

        BalanceResponse response = driverBalanceService.getDriverBalance(DRIVER_ID);

        assertNotNull(response);
        assertEquals(INITIAL_BALANCE, response.balance());
    }

    @Test
    void shouldWithdrawFromDriverBalance() {
        driverBalance.setBalance(BALANCE_AMOUNT);

        when(driverBalanceRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driverBalance));

        BigDecimal withdrawnAmount = driverBalanceService.getFromDriverBalance(DRIVER_ID, BALANCE_AMOUNT);

        assertEquals(BALANCE_AMOUNT, withdrawnAmount);
        assertEquals(BigDecimal.ZERO.setScale(2), driverBalance.getBalance());
        verify(driverBalanceRepository, times(1)).save(driverBalance);
    }

    @Test
    void shouldThrowExceptionWhenWithdrawingMoreThanAvailable() {
        driverBalance.setBalance(BALANCE_AMOUNT);

        when(driverBalanceRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driverBalance));

        Exception exception = assertThrows(InsufficientBalanceException.class, () ->
                driverBalanceService.getFromDriverBalance(DRIVER_ID, EXCESS_AMOUNT));

        assertEquals(EXCEPTION_INSUFFICIENT_BALANCE, exception.getMessage());
    }
}

