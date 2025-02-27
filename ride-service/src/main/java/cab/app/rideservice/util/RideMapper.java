package cab.app.rideservice.util;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.model.Ride;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RideMapper {
    Ride toEntity(RideRequest rideRequest);

    RideResponse toDto(Ride ride);
}
