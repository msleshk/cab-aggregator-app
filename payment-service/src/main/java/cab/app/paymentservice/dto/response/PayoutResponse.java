package cab.app.paymentservice.dto.response;

import java.math.BigDecimal;

public record PayoutResponse(
        Long payoutId,
        BigDecimal amount
) {
}
