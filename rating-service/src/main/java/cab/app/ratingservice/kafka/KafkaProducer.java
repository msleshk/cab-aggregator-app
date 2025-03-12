package cab.app.ratingservice.kafka;

import cab.app.ratingservice.dto.response.AverageRating;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static cab.app.ratingservice.kafka.util.Constants.DRIVER_TOPIC;
import static cab.app.ratingservice.kafka.util.Constants.PASSENGER_TOPIC;
import static org.reflections.Reflections.log;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPassengerAvgRating(AverageRating passengerAvgRating){
        kafkaTemplate.send(PASSENGER_TOPIC, passengerAvgRating);
        System.out.println("Sending message");
        logger(passengerAvgRating);
    }

    public void sendDriverAvgTaring(AverageRating driverAvgRating){
        kafkaTemplate.send(DRIVER_TOPIC, driverAvgRating);
        System.out.println("sending message");
        logger(driverAvgRating);
    }

    private void logger(AverageRating avgRatingResponse){
        log.info("Send message{}", avgRatingResponse);
    }
}
