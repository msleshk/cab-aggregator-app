package cab.app.paymentservice.controller;

import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import cab.app.paymentservice.service.PayoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payout")
@RequiredArgsConstructor
public class PayoutController {

    private final PayoutService payoutService;

    @PostMapping
    public ResponseEntity<PayoutResponse> getPayout(@RequestBody @Valid PayoutRequest payoutRequest) {
        return ResponseEntity.ok().body(payoutService.createPayout(payoutRequest));
    }
}
