package cab.app.ratingservice.exception;

public class RideNotCompletedException extends RuntimeException{
    public RideNotCompletedException(String message){
        super(message);
    }
}
