package cab.app.paymentservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequest(
        @NotNull(message = "Amount cannot be null")
        @Min(value = 1, message = "Amount must be at least 1")
        BigDecimal amount
) {
}

