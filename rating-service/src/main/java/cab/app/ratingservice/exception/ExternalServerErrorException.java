package cab.app.ratingservice.exception;

public class ExternalServerErrorException extends RuntimeException {
    public ExternalServerErrorException(String message) {
        super(message);
    }
}
