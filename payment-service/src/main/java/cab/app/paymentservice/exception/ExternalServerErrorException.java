package cab.app.paymentservice.exception;

public class ExternalServerErrorException extends RuntimeException {
    public ExternalServerErrorException(String message) {
        super(message);
    }
}
