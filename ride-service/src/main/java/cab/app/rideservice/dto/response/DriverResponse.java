package cab.app.rideservice.dto.response;

import cab.app.rideservice.model.enums.DriverStatus;

public record DriverResponse(
        Long id,
        DriverStatus status,
        Long carId
) {
}
