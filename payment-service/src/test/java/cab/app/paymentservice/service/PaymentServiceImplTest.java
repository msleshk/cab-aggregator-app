package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.dto.response.ResponseList;
import cab.app.paymentservice.exception.*;
import cab.app.paymentservice.model.Payment;
import cab.app.paymentservice.model.enums.PaymentStatus;
import cab.app.paymentservice.repository.PaymentRepository;
import cab.app.paymentservice.service.implementation.PaymentServiceImpl;
import cab.app.paymentservice.util.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static cab.app.paymentservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private DriverBalanceService driverBalanceService;

    @Mock
    private PassengerBalanceService passengerBalanceService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(DRIVER_ID);
        payment.setPassengerId(PASSENGER_ID);
        payment.setCost(BALANCE_AMOUNT);
    }

    @Test
    void shouldCreatePayment() {
        CreatePaymentRequest request = mock(CreatePaymentRequest.class);
        when(request.driverId()).thenReturn(DRIVER_ID);

        when(paymentRepository.findByRideId(anyLong())).thenReturn(Optional.empty());
        when(paymentMapper.toEntity(request)).thenReturn(payment);

        paymentService.createPayment(request);

        verify(paymentRepository, times(1)).save(payment);
    }


    @Test
    void shouldThrowExceptionIfPaymentAlreadyExists() {
        CreatePaymentRequest request = mock(CreatePaymentRequest.class);
        when(paymentRepository.findByRideId(anyLong())).thenReturn(Optional.of(payment));

        assertThrows(PaymentAlreadyExistException.class, () -> paymentService.createPayment(request));
    }

    @Test
    void shouldDeletePayment() {
        when(paymentRepository.findById(DRIVER_ID)).thenReturn(Optional.of(payment));

        paymentService.deletePayment(DRIVER_ID);

        verify(paymentRepository, times(1)).delete(payment);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPayment() {
        when(paymentRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.deletePayment(DRIVER_ID));
    }

    @Test
    void shouldReturnAllPayments() {
        Page<Payment> page = new PageImpl<>(Collections.singletonList(payment));
        when(paymentRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(paymentMapper.toDto(payment)).thenReturn(mock(PaymentResponse.class));

        ResponseList<PaymentResponse> response = paymentService.getAllPayments(0, 10);

        assertNotNull(response);
        assertFalse(response.responseList().isEmpty());
    }

    @Test
    void shouldPayForRideSuccessfully() {
        PayRequest payRequest = mock(PayRequest.class);
        when(payRequest.rideId()).thenReturn(RIDE_ID);
        when(payRequest.passengerId()).thenReturn(PASSENGER_ID);
        when(payRequest.promoCode()).thenReturn(null);

        payment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findByRideId(RIDE_ID)).thenReturn(Optional.of(payment));

        paymentService.payForRide(payRequest);

        assertEquals(PaymentStatus.PAID, payment.getStatus());
        verify(passengerBalanceService).withdrawFromPassengerBalance(PASSENGER_ID, payment.getCost());
        verify(driverBalanceService).increaseDriverBalance(payment.getDriverId(), payment.getCost());
    }

    @Test
    void shouldThrowExceptionWhenPayingWithWrongPassengerId() {
        PayRequest payRequest = mock(PayRequest.class);
        when(payRequest.rideId()).thenReturn(RIDE_ID);
        when(payRequest.passengerId()).thenReturn(WRONG_ID);

        when(paymentRepository.findByRideId(RIDE_ID)).thenReturn(Optional.of(payment));

        assertThrows(IllegalArgumentException.class, () -> paymentService.payForRide(payRequest));
    }

    @Test
    void shouldReturnPaymentById() {
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDto(payment)).thenReturn(mock(PaymentResponse.class));

        PaymentResponse response = paymentService.getPaymentById(PAYMENT_ID);

        assertNotNull(response);
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFoundById() {
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> paymentService.getPaymentById(PAYMENT_ID));
    }

}

