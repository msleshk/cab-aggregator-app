package cab.app.rideservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String PAYMENT_TOPIC = "payment-topic";
    public static final String DRIVER_STATUS_TOPIC = "driver-status-update";
}
