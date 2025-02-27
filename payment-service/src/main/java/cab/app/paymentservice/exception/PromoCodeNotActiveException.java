package cab.app.paymentservice.exception;

public class PromoCodeNotActiveException extends RuntimeException {
    public PromoCodeNotActiveException(String message) {
        super(message);
    }
}
