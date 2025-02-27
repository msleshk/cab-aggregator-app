package cab.app.ratingservice.exception;

public class RatingAlreadyExistException extends RuntimeException{
    public RatingAlreadyExistException(String message){
        super(message);
    }
}
