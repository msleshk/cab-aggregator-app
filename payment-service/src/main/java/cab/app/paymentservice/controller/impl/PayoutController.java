package cab.app.paymentservice.controller.impl;

import cab.app.paymentservice.controller.PayoutApi;
import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import cab.app.paymentservice.service.PayoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PayoutController implements PayoutApi {

    private final PayoutService payoutService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<PayoutResponse> getPayout(PayoutRequest payoutRequest) {
        return ResponseEntity.ok().body(payoutService.createPayout(payoutRequest));
    }
}

