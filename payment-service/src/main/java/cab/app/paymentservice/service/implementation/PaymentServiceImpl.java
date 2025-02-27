package cab.app.paymentservice.service.implementation;

import cab.app.paymentservice.dto.request.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.exception.*;
import cab.app.paymentservice.model.Payment;
import cab.app.paymentservice.model.PromoCode;
import cab.app.paymentservice.model.enums.PaymentStatus;
import cab.app.paymentservice.repository.PaymentRepository;
import cab.app.paymentservice.repository.PromoCodeRepository;
import cab.app.paymentservice.service.DriverBalanceService;
import cab.app.paymentservice.service.PaymentService;
import cab.app.paymentservice.util.PaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PromoCodeRepository promoCodeRepository;
    private final DriverBalanceService driverBalanceService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, PromoCodeRepository promoCodeRepository, DriverBalanceService driverBalanceService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.promoCodeRepository = promoCodeRepository;
        this.driverBalanceService = driverBalanceService;
    }

    @Override
    @Transactional
    public void createPayment(CreatePaymentRequest paymentRequest) {
        if (paymentRepository.findByRideId(paymentRequest.getRideId()).isPresent()) {
            throw new PaymentAlreadyExistException("Payment for this ride already exist!");
        }
        BigDecimal finalAmount = paymentRequest.getCost();
        if (paymentRequest.getPromoCode() != null) {
            finalAmount = applyPromoCode(paymentRequest.getPromoCode(), finalAmount);
        }
        Payment payment = paymentMapper.toEntity(paymentRequest);

        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setFinalCost(finalAmount);

        driverBalanceService.createDriverBalance(payment.getDriverId());

        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new EntityNotFoundException("Payment not found!"));
        paymentRepository.delete(payment);
    }

    @Override
    @Transactional
    public PayResponse payForRide(PayRequest payRequest) {
        Payment payment = paymentRepository.findByRideId(payRequest.getRideId()).orElseThrow(() -> new EntityNotFoundException("Payment not found!"));

        if (!payment.getPassengerId().equals(payRequest.getPassengerId())) {
            throw new IllegalArgumentException("Wrong passenger id!");
        }
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);

        driverBalanceService.increaseDriverBalance(payment.getDriverId(), payment.getCost());

        return new PayResponse(payment.getId(), payment.getStatus().toString());
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream().map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        return paymentMapper.toDto(paymentRepository.findById(paymentId).orElseThrow(() -> new EntityNotFoundException("Payment not found!")));
    }

    @Override
    public PaymentResponse getPaymentByRide(Long rideId) {
        return paymentMapper.toDto(paymentRepository.findByRideId(rideId).orElseThrow(() -> new EntityNotFoundException("Payment not found!")));
    }

    private BigDecimal applyPromoCode(String paymentPromoCode, BigDecimal finalAmount) {
        PromoCode promoCode = promoCodeRepository.findByCode(paymentPromoCode)
                .orElseThrow(() -> new EntityNotFoundException("Invalid promo code"));

        if (!promoCode.isActive()) {
            throw new PromoCodeNotActiveException("Promo code is not active");
        }
        BigDecimal discount = finalAmount.multiply(promoCode.getDiscountAmount().divide(BigDecimal.valueOf(100)));
        finalAmount = finalAmount.subtract(discount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }
        return finalAmount;
    }
}
