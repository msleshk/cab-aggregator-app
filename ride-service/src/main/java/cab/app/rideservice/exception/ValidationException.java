package cab.app.rideservice.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}
