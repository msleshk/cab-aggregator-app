package cab.app.ratingservice.service.implementation;

import cab.app.ratingservice.client.driver.DriverClientContainer;
import cab.app.ratingservice.client.passenger.PassengerClientContainer;
import cab.app.ratingservice.client.ride.RideClientContainer;
import cab.app.ratingservice.dto.response.ride.RideResponse;
import cab.app.ratingservice.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityValidator {
    private final DriverClientContainer driverClient;
    private final PassengerClientContainer passengerClient;
    private final RideClientContainer rideClient;

    public void checkIfUserExist(Long userId, Role role){
        switch (role){
            case PASSENGER -> checkIfPassengerExist(userId);
            case DRIVER -> checkIfDriverExist(userId);
        }
    }

    private void checkIfDriverExist(Long driverId) {
        driverClient.getDriverById(driverId);
    }

    private void checkIfPassengerExist(Long passengerId) {
        passengerClient.getPassengerById(passengerId);
    }

    public RideResponse getRideById(Long rideId) {
        return rideClient.getRideById(rideId);
    }
}
