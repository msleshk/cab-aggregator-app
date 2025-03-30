package cab.app.paymentservice.service.implementation;

import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.dto.response.ResponseList;
import cab.app.paymentservice.exception.*;
import cab.app.paymentservice.model.Payment;
import cab.app.paymentservice.model.PromoCode;
import cab.app.paymentservice.model.enums.PaymentStatus;
import cab.app.paymentservice.repository.PaymentRepository;
import cab.app.paymentservice.repository.PromoCodeRepository;
import cab.app.paymentservice.service.DriverBalanceService;
import cab.app.paymentservice.service.PassengerBalanceService;
import cab.app.paymentservice.service.PaymentService;
import cab.app.paymentservice.util.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PromoCodeRepository promoCodeRepository;
    private final DriverBalanceService driverBalanceService;
    private final PassengerBalanceService passengerBalanceService;

    @Override
    @Transactional
    public void createPayment(CreatePaymentRequest paymentRequest) {
        validatePayment(paymentRequest);
        Payment payment = paymentMapper.toEntity(paymentRequest);

        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
    }

    private void validatePayment(CreatePaymentRequest paymentRequest) {
        if (paymentRepository.findByRideId(paymentRequest.driverId()).isPresent()) {
            throw new PaymentAlreadyExistException("Payment for this ride already exist!");
        }
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = findPaymentById(paymentId);
        paymentRepository.delete(payment);
    }

    @Override
    @Transactional
    public PayResponse payForRide(PayRequest payRequest) {
        Payment payment = findPaymentByRideId(payRequest.rideId());

        BigDecimal finalAmount = payment.getCost();
        if (payRequest.promoCode() != null) {
            finalAmount = applyPromoCode(payRequest.promoCode(), finalAmount);
        }
        if (!payment.getPassengerId().equals(payRequest.passengerId())) {
            throw new IllegalArgumentException("Wrong passenger id!");
        }
        passengerBalanceService.withdrawFromPassengerBalance(payRequest.passengerId(), finalAmount);
        payment.setStatus(PaymentStatus.PAID);
        payment.setFinalCost(finalAmount);
        paymentRepository.save(payment);

        driverBalanceService.increaseDriverBalance(payment.getDriverId(), payment.getCost());

        return new PayResponse(payment.getId(), payment.getStatus().toString());
    }

    @Override
    public ResponseList<PaymentResponse> getAllPayments(int offset, int limit) {
        return new ResponseList<>(paymentRepository.findAll(PageRequest.of(offset, limit))
                .getContent()
                .stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList())
        );
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        return paymentMapper.toDto(findPaymentById(paymentId));
    }

    @Override
    public PaymentResponse getPaymentByRide(Long rideId) {
        return paymentMapper.toDto(findPaymentByRideId(rideId));
    }

    private Payment findPaymentByRideId(Long rideId) {
        return paymentRepository.findByRideId(rideId).orElseThrow(()
                -> new EntityNotFoundException("Payment not found!"));
    }

    private Payment findPaymentById(Long id) {
        return paymentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Payment not found!"));
    }

    private BigDecimal applyPromoCode(String paymentPromoCode, BigDecimal finalAmount) {
        PromoCode promoCode = promoCodeRepository.findByCode(paymentPromoCode)
                .orElseThrow(() -> new EntityNotFoundException("Invalid promo code"));

        if (!promoCode.isActive()) {
            throw new PromoCodeNotActiveException("Promo code is not active");
        }
        BigDecimal discount = finalAmount.multiply(BigDecimal.valueOf(promoCode.getDiscountAmount() / 100));
        finalAmount = finalAmount.subtract(discount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }
        return finalAmount;
    }
}
