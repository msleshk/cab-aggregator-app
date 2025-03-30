package cab.app.ratingservice.service;

import cab.app.ratingservice.client.driver.DriverClient;
import cab.app.ratingservice.client.passenger.PassengerClient;
import cab.app.ratingservice.client.ride.RideClient;
import cab.app.ratingservice.model.enums.Role;
import cab.app.ratingservice.service.implementation.EntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cab.app.ratingservice.dto.response.ride.RideResponse;

import static cab.app.ratingservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityValidatorTest {

    @Mock
    private DriverClient driverClient;

    @Mock
    private PassengerClient passengerClient;

    @Mock
    private RideClient rideClient;

    @InjectMocks
    private EntityValidator entityValidator;

    @Test
    void shouldCheckIfDriverExists() {
        doNothing().when(driverClient).getDriverById(USER_ID);

        entityValidator.checkIfUserExist(USER_ID, Role.valueOf(DRIVER_ROLE));

        verify(driverClient, times(1)).getDriverById(USER_ID);
    }

    @Test
    void shouldCheckIfPassengerExists() {
        Long passengerId = RATED_USER_ID;
        doNothing().when(passengerClient).getPassengerById(passengerId);

        entityValidator.checkIfUserExist(passengerId, Role.valueOf(PASSENGER_ROLE));

        verify(passengerClient, times(1)).getPassengerById(passengerId);
    }

    @Test
    void shouldGetRideById() {
        Long rideId = RIDE_ID;
        RideResponse rideResponse = new RideResponse(RIDE_ID, USER_ID, USER_ID, RIDE_STATUS_COMPLETED);
        when(rideClient.getRideById(rideId)).thenReturn(rideResponse);

        RideResponse result = entityValidator.getRideById(rideId);

        assertNotNull(result);
        verify(rideClient, times(1)).getRideById(rideId);
    }
}
