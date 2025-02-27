package cab.app.paymentservice.exception;

public class PaymentAlreadyExistException extends RuntimeException {
    public PaymentAlreadyExistException(String message) {
        super(message);
    }
}
