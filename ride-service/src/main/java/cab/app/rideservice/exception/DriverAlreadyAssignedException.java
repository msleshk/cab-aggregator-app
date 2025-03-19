package cab.app.rideservice.exception;

public class DriverAlreadyAssignedException extends RuntimeException {
    public DriverAlreadyAssignedException(String message) {
        super(message);
    }
}
