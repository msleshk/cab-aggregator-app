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
        try {
            driverClient.getDriverById(id);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("Driver with id " + id + " not found. " + ex.getMessage());
        }
    }

    public void checkIfPassengerExist(Long id) {
        try {
            passengerClient.getPassengerById(id);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("Passenger with id " + id + " not found. " + ex.getMessage());
        }
    }
}
