package cab.app.rideservice.exception;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(String message){
        super(message);
    }
}
