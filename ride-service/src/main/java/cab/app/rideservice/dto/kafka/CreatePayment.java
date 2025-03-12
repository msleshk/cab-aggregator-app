package cab.app.rideservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreatePayment(
        Long rideId,

        Long passengerId,

        Long driverId,

        BigDecimal cost
) {
}
