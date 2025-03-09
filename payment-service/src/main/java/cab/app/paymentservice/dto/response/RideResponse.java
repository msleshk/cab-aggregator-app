package cab.app.paymentservice.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RideResponse(
        Long rideId,
        Long driverId,
        Long passengerId,
        BigDecimal cost
) {

}
