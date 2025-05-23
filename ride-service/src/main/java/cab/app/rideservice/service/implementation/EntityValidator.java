package cab.app.rideservice.service.implementation;

import cab.app.rideservice.client.driver.DriverClient;
import cab.app.rideservice.client.driver.DriverClientContainer;
import cab.app.rideservice.client.passenger.PassengerClient;
import cab.app.rideservice.client.passenger.PassengerClientContainer;
import cab.app.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityValidator {
    private final DriverClientContainer driverClient;
    private final PassengerClientContainer passengerClient;

    public void checkIfPassengerExist(Long id) {
        passengerClient.getPassengerById(id);
    }

    public DriverResponse getDriverById(Long id){return driverClient.getDriverById(id);}
}
