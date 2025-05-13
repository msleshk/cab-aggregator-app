package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.exception.EntityNotFoundException;
import cab.app.paymentservice.exception.InsufficientBalanceException;
import cab.app.paymentservice.model.PassengerBalance;
import cab.app.paymentservice.repository.PassengerBalanceRepository;
import cab.app.paymentservice.service.implementation.PassengerBalanceServiceImpl;
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
class PassengerBalanceServiceImplTest {

    @Mock
    private PassengerBalanceRepository passengerBalanceRepository;

    @Mock
    private BalanceMapper balanceMapper;

    @InjectMocks
    private PassengerBalanceServiceImpl passengerBalanceService;

    private PassengerBalance passengerBalance;

    @BeforeEach
    void setUp() {
        passengerBalance = new PassengerBalance(PASSENGER_ID, INITIAL_BALANCE);
    }

    @Test
    void shouldCreatePassengerBalanceIfNotExists() {
        when(passengerBalanceRepository.existsById(PASSENGER_ID)).thenReturn(false);

        passengerBalanceService.createPassengerBalance(PASSENGER_ID);

        verify(passengerBalanceRepository, times(1)).save(any(PassengerBalance.class));
    }

    @Test
    void shouldNotCreatePassengerBalanceIfAlreadyExists() {
        when(passengerBalanceRepository.existsById(PASSENGER_ID)).thenReturn(true);

        passengerBalanceService.createPassengerBalance(PASSENGER_ID);

        verify(passengerBalanceRepository, never()).save(any(PassengerBalance.class));
    }

    @Test
    void shouldIncreasePassengerBalance() {
        when(passengerBalanceRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passengerBalance));

        passengerBalanceService.increasePassengerBalance(PASSENGER_ID, BALANCE_AMOUNT);

        assertEquals(0, passengerBalance.getBalance().compareTo(INITIAL_BALANCE.add(BALANCE_AMOUNT)));
        verify(passengerBalanceRepository, times(1)).save(passengerBalance);
    }

    @Test
    void shouldWithdrawFromPassengerBalance() {
        passengerBalance.setBalance(BALANCE_AMOUNT);
        when(passengerBalanceRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passengerBalance));

        passengerBalanceService.withdrawFromPassengerBalance(PASSENGER_ID, BALANCE_AMOUNT);

        assertEquals(0, passengerBalance.getBalance().compareTo(BigDecimal.ZERO));
        verify(passengerBalanceRepository, times(1)).save(passengerBalance);
    }

    @Test
    void shouldThrowExceptionWhenWithdrawingMoreThanBalance() {
        when(passengerBalanceRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passengerBalance));

        assertThrows(InsufficientBalanceException.class, () ->
                passengerBalanceService.withdrawFromPassengerBalance(PASSENGER_ID, BALANCE_AMOUNT)
        );

        verify(passengerBalanceRepository, never()).save(any(PassengerBalance.class));
    }

    @Test
    void shouldReturnPassengerBalance() {
        when(passengerBalanceRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passengerBalance));
        when(balanceMapper.toDto(passengerBalance)).thenReturn(new BalanceResponse(PASSENGER_ID, INITIAL_BALANCE));

        BalanceResponse response = passengerBalanceService.getPassengerBalance(PASSENGER_ID);

        assertNotNull(response);
        assertEquals(PASSENGER_ID, response.id());
        assertEquals(INITIAL_BALANCE, response.balance());
    }

    @Test
    void shouldThrowExceptionWhenPassengerBalanceNotFound() {
        when(passengerBalanceRepository.findById(PASSENGER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                passengerBalanceService.getPassengerBalance(PASSENGER_ID)
        );
    }
}

