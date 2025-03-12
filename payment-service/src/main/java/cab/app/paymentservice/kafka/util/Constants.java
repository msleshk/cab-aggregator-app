package cab.app.paymentservice.kafka.util;

public class Constants {
    public static final String PAYMENT_GROUP = "payment-group";
    public static final String PAYMENT_TOPIC = "payment-topic";
    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String TRUSTED_PACKAGES = "cab.app.paymentservice.dto.request";
    public static final String DESERIALIZE_PAYMENT_REQUEST = "cab.app.rideservice.dto.kafka.CreatePayment" +
            ":cab.app.paymentservice.dto.request.CreatePaymentRequest";
}
