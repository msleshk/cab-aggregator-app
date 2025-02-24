package cab.app.rideservice.service.implementation;

import cab.app.rideservice.exception.InvalidStatusException;
import cab.app.rideservice.model.enums.RideStatus;
import org.springframework.stereotype.Component;

@Component
public class RideStatusValidator {
    public RideStatus validateRideStatus(RideStatus currentStatus, RideStatus newStatus){
        switch (currentStatus){
            case REQUESTED:
                if (newStatus == RideStatus.ACCEPTED || newStatus == RideStatus.CANCELLED){
                    return newStatus;
                }
                throwInvalidStatusExc("Status can only be changed to ACCEPTED or CANCELLED!");
            case ACCEPTED:
                if (newStatus == RideStatus.IN_PROGRESS || newStatus == RideStatus.CANCELLED){
                    return newStatus;
                }
                throwInvalidStatusExc("Status can only be changed to IN_PROGRESS or CANCELLED!");
            case IN_PROGRESS:
                if (newStatus == RideStatus.COMPLETED || newStatus == RideStatus.CANCELLED){
                    return newStatus;
                }
                throwInvalidStatusExc("Status can only be changed to COMPLETED or CANCELLED!");
            case COMPLETED:
            case CANCELLED:
                throwInvalidStatusExc("Status cannot be changed!");
        }
        return currentStatus;
    }

    private void throwInvalidStatusExc(String message){
        throw new InvalidStatusException(message);
    }
}
