package cab.app.ratingservice.service.implementation;

import cab.app.ratingservice.client.driver.DriverClient;
import cab.app.ratingservice.client.passenger.PassengerClient;
import cab.app.ratingservice.client.ride.RideClient;
import cab.app.ratingservice.dto.response.ride.RideResponse;
import cab.app.ratingservice.exception.EntityNotFoundException;
import cab.app.ratingservice.model.enums.Role;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityValidator {
    private final DriverClient driverClient;
    private final PassengerClient passengerClient;
    private final RideClient rideClient;

    public void checkIfUserExist(Long userId, Role role){
        switch (role){
            case PASSENGER -> checkIfPassengerExist(userId);
            case DRIVER -> checkIfDriverExist(userId);
        }
    }

    private void checkIfDriverExist(Long driverId) {
        try {
            driverClient.getDriverById(driverId);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
    }

    private void checkIfPassengerExist(Long passengerId) {
        try {
            passengerClient.getPassengerById(passengerId);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
    }

    public RideResponse getRideById(Long rideId) {
        try {
            return rideClient.getRideById(rideId);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
    }
}
