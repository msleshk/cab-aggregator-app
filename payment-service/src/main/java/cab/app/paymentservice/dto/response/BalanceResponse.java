package cab.app.paymentservice.dto.response;

import java.math.BigDecimal;

public record BalanceResponse(
        Long id,
        BigDecimal balance
) {
}
