package cab.app.ratingservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String DRIVER_TOPIC = "driver-rating-topic";
    public static final String PASSENGER_TOPIC = "passenger-rating-topic";
}
