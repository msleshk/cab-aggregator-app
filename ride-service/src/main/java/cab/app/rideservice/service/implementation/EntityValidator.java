package cab.app.rideservice.service.implementation;

import cab.app.rideservice.client.driver.DriverClient;
import cab.app.rideservice.client.passenger.PassengerClient;
import cab.app.rideservice.exception.EntityNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityValidator {
    private final DriverClient driverClient;
    private final PassengerClient passengerClient;

    public void checkIfDriverExist(Long id) {
        driverClient.getDriverById(id);
    }

    public void checkIfPassengerExist(Long id) {
        passengerClient.getPassengerById(id);
    }
}
