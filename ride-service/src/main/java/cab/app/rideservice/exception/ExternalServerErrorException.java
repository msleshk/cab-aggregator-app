package cab.app.rideservice.exception;

public class ExternalServerErrorException extends RuntimeException {
    public ExternalServerErrorException(String message) {
        super(message);
    }
}
