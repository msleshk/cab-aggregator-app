package cab.app.paymentservice.controller;

import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import cab.app.paymentservice.exception.ValidationException;
import cab.app.paymentservice.service.PayoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payout")
public class PayoutController {
    private final PayoutService payoutService;

    public PayoutController(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    @PostMapping
    public ResponseEntity<PayoutResponse> getPayout(@RequestBody @Valid PayoutRequest payoutRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        return ResponseEntity.ok().body(payoutService.createPayout(payoutRequest));
    }
}
