package cab.app.paymentservice.service.implementation;

import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import cab.app.paymentservice.model.Payout;
import cab.app.paymentservice.repository.PayoutRepository;
import cab.app.paymentservice.service.DriverBalanceService;
import cab.app.paymentservice.service.PayoutService;
import cab.app.paymentservice.util.PayoutMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional
public class PayoutServiceImpl implements PayoutService {
    private final DriverBalanceService driverBalanceService;
    private final PayoutRepository payoutRepository;
    private final PayoutMapper payoutMapper;

    public PayoutServiceImpl(DriverBalanceService driverBalanceService, PayoutRepository payoutRepository, PayoutMapper payoutMapper) {
        this.driverBalanceService = driverBalanceService;
        this.payoutRepository = payoutRepository;
        this.payoutMapper = payoutMapper;
    }

    @Override
    public PayoutResponse createPayout(PayoutRequest payoutRequest) {
        Payout payout = payoutMapper.toEntity(payoutRequest);

        payout.setAmount(driverBalanceService
                .getFromDriverBalance(payout.getDriverId(), payout.getAmount()));

        payout.setCreatedAt(LocalDateTime.now());

        payout = payoutRepository.save(payout);

        return payoutMapper.toDto(payout);
    }
}
