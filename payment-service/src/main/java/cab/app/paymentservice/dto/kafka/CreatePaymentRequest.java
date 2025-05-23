package cab.app.paymentservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
public record CreatePaymentRequest(
        @NotNull(message = "Ride ID cannot be null")
        Long rideId,

        @NotNull(message = "Passenger ID cannot be null")
        Long passengerId,

        @NotNull(message = "Driver ID cannot be null")
        Long driverId,

        @NotNull(message = "Cost should not be null")
        BigDecimal cost
) {

}
