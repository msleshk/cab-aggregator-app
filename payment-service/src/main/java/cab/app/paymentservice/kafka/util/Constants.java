package cab.app.paymentservice.kafka.util;

public class Constants {
    public static final String PAYMENT_GROUP = "payment-group";
    public static final String PASSENGER_BALANCE_GROUP = "passenger-balance-group";
    public static final String DRIVER_BALANCE_GROUP = "driver-balance-group";
    public static final String DRIVER_BALANCE_TOPIC = "driver-balance-topic";
    public static final String PASSENGER_BALANCE_TOPIC = "passenger-balance-topic";
    public static final String PAYMENT_TOPIC = "payment-topic";
    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String TRUSTED_PACKAGES = "cab.app.paymentservice.dto.kafka, com.example.passengerservice.dto.kafka, com.example.driverservice.dto.kafka";
    private static final String DESERIALIZE_PAYMENT_REQUEST = "cab.app.rideservice.dto.kafka.CreatePayment" +
            ":cab.app.paymentservice.dto.kafka.CreatePaymentRequest";
    private static final String DESERIALIZE_PASSENGER_BALANCE_REQUEST = "com.example.passengerservice.dto.kafka.NewPassengerBalance" +
            ":cab.app.paymentservice.dto.kafka.BalanceRequest";
    private static final String DESERIALIZE_DRIVER_BALANCE_REQUEST = "com.example.driverservice.dto.kafka.NewDriverBalance" +
            ":cab.app.paymentservice.dto.kafka.BalanceRequest";
    public static final String DESERIALIZE_TYPE_MAPPINGS = DESERIALIZE_DRIVER_BALANCE_REQUEST + ", " + DESERIALIZE_PASSENGER_BALANCE_REQUEST + ", " + DESERIALIZE_PAYMENT_REQUEST;
}
