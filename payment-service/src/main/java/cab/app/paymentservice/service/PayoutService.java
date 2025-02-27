package cab.app.paymentservice.service;

import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;

public interface PayoutService {
    PayoutResponse createPayout(PayoutRequest payoutRequest);
}
