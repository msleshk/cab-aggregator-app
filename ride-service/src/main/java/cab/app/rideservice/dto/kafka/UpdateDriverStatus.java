package cab.app.rideservice.dto.kafka;

import cab.app.rideservice.model.enums.DriverStatus;
import lombok.Builder;

@Builder
public record UpdateDriverStatus(
        Long driverId,
        DriverStatus status
) {
}
