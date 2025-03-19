package cab.app.rideservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RideToUpdate(
        @NotNull(message = "Departure address cannot not be null!")
        String departureAddress,
        @NotNull(message = "Arrival address cannot be null!")
        String arrivalAddress,
        @NotNull(message = "Distance cannot be null!")
        @Positive(message = "Distance cannot be less than 0!")
        BigDecimal distance
) {
}
