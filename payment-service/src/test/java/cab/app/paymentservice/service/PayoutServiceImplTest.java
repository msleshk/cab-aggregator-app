package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import cab.app.paymentservice.model.Payout;
import cab.app.paymentservice.repository.PayoutRepository;
import cab.app.paymentservice.service.implementation.PayoutServiceImpl;
import cab.app.paymentservice.util.PayoutMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static cab.app.paymentservice.utils.TestConstants.BALANCE_AMOUNT;
import static cab.app.paymentservice.utils.TestConstants.DRIVER_ID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayoutServiceImplTest {

    @Mock
    private DriverBalanceService driverBalanceService;

    @Mock
    private PayoutRepository payoutRepository;

    @Mock
    private PayoutMapper payoutMapper;

    @InjectMocks
    private PayoutServiceImpl payoutService;

    private Payout payout;
    private PayoutRequest payoutRequest;
    private PayoutResponse payoutResponse;

    @BeforeEach
    void setUp() {
        payoutRequest = mock(PayoutRequest.class);
        payout = new Payout();
        payout.setDriverId(DRIVER_ID);
        payout.setAmount(BALANCE_AMOUNT);
        payout.setCreatedAt(LocalDateTime.now());

        payoutResponse = mock(PayoutResponse.class);
    }

    @Test
    void shouldCreatePayoutSuccessfully() {
        when(payoutMapper.toEntity(payoutRequest)).thenReturn(payout);
        when(driverBalanceService.getFromDriverBalance(DRIVER_ID, BALANCE_AMOUNT))
                .thenReturn(BALANCE_AMOUNT);
        when(payoutRepository.save(payout)).thenReturn(payout);
        when(payoutMapper.toDto(payout)).thenReturn(payoutResponse);

        PayoutResponse response = payoutService.createPayout(payoutRequest);

        assertNotNull(response);
        verify(driverBalanceService, times(1))
                .getFromDriverBalance(DRIVER_ID, BALANCE_AMOUNT);
        verify(payoutRepository, times(1)).save(payout);
        verify(payoutMapper, times(1)).toDto(payout);
    }
}

