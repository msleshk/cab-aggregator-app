package cab.app.rideservice.service;

import cab.app.rideservice.exception.InvalidStatusException;
import cab.app.rideservice.model.enums.RideStatus;
import cab.app.rideservice.service.implementation.RideStatusValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cab.app.rideservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class RideStatusValidatorTest {

    private RideStatusValidator rideStatusValidator;

    @BeforeEach
    void setUp() {
        rideStatusValidator = new RideStatusValidator();
    }

    @Test
    void shouldAllowRequestedToAccepted() {
        RideStatus result = rideStatusValidator.validateRideStatus(RideStatus.REQUESTED, RideStatus.ACCEPTED);
        assertEquals(RideStatus.ACCEPTED, result);
    }

    @Test
    void shouldAllowRequestedToCancelled() {
        RideStatus result = rideStatusValidator.validateRideStatus(RideStatus.REQUESTED, RideStatus.CANCELLED);
        assertEquals(RideStatus.CANCELLED, result);
    }

    @Test
    void shouldThrowExceptionForInvalidRequestedStatus() {
        InvalidStatusException exception = assertThrows(InvalidStatusException.class,
                () -> rideStatusValidator.validateRideStatus(RideStatus.REQUESTED, RideStatus.IN_PROGRESS)
        );
        assertEquals(ERROR_STATUS_REQUESTED, exception.getMessage());
    }

    @Test
    void shouldAllowAcceptedToInProgress() {
        RideStatus result = rideStatusValidator.validateRideStatus(RideStatus.ACCEPTED, RideStatus.IN_PROGRESS);
        assertEquals(RideStatus.IN_PROGRESS, result);
    }

    @Test
    void shouldAllowAcceptedToCancelled() {
        RideStatus result = rideStatusValidator.validateRideStatus(RideStatus.ACCEPTED, RideStatus.CANCELLED);
        assertEquals(RideStatus.CANCELLED, result);
    }

    @Test
    void shouldThrowExceptionForInvalidAcceptedStatus() {
        InvalidStatusException exception = assertThrows(InvalidStatusException.class,
                () -> rideStatusValidator.validateRideStatus(RideStatus.ACCEPTED, RideStatus.COMPLETED)
        );
        assertEquals(ERROR_STATUS_ACCEPTED, exception.getMessage());
    }

    @Test
    void shouldAllowInProgressToCompleted() {
        RideStatus result = rideStatusValidator.validateRideStatus(RideStatus.IN_PROGRESS, RideStatus.COMPLETED);
        assertEquals(RideStatus.COMPLETED, result);
    }

    @Test
    void shouldAllowInProgressToCancelled() {
        RideStatus result = rideStatusValidator.validateRideStatus(RideStatus.IN_PROGRESS, RideStatus.CANCELLED);
        assertEquals(RideStatus.CANCELLED, result);
    }

    @Test
    void shouldThrowExceptionForInvalidInProgressStatus() {
        InvalidStatusException exception = assertThrows(InvalidStatusException.class,
                () -> rideStatusValidator.validateRideStatus(RideStatus.IN_PROGRESS, RideStatus.ACCEPTED)
        );
        assertEquals(ERROR_STATUS_IN_PROGRESS, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForChangingCompletedStatus() {
        InvalidStatusException exception = assertThrows(InvalidStatusException.class,
                () -> rideStatusValidator.validateRideStatus(RideStatus.COMPLETED, RideStatus.REQUESTED)
        );
        assertEquals(ERROR_STATUS_FINAL, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForChangingCancelledStatus() {
        InvalidStatusException exception = assertThrows(InvalidStatusException.class,
                () -> rideStatusValidator.validateRideStatus(RideStatus.CANCELLED, RideStatus.ACCEPTED)
        );
        assertEquals(ERROR_STATUS_FINAL, exception.getMessage());
    }
}

