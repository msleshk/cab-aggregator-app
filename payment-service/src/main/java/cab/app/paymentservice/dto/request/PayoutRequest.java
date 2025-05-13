package cab.app.paymentservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
public record PayoutRequest(
        @NotNull(message = "Driver ID cannot be null")
        Long driverId,

        @NotNull(message = "Amount cannot be null")
        @Min(value = 1, message = "Amount must be at least 1")
        BigDecimal amount
) {
}
