package cab.app.paymentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
public record PayRequest(
        @NotNull(message = "Ride ID cannot be null")
        Long rideId,

        @NotNull(message = "Passenger ID cannot be null")
        Long passengerId,

        String promoCode
) {

}
