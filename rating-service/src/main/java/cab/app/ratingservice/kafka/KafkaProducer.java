package cab.app.ratingservice.kafka;

import cab.app.ratingservice.dto.response.AverageRating;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static cab.app.ratingservice.kafka.util.Constants.DRIVER_TOPIC;
import static cab.app.ratingservice.kafka.util.Constants.PASSENGER_TOPIC;
import static org.reflections.Reflections.log;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final Tracer tracer;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPassengerAvgRating(AverageRating passengerAvgRating){
        String traceId = getTraceId();

        Message<AverageRating> message = MessageBuilder
                .withPayload(passengerAvgRating)
                .setHeader(KafkaHeaders.TOPIC, PASSENGER_TOPIC)
                .setHeader("traceId", traceId)
                .build();

        kafkaTemplate.send(message);
    }

    public void sendDriverAvgTaring(AverageRating driverAvgRating){
        String traceId = getTraceId();

        Message<AverageRating> message = MessageBuilder
                .withPayload(driverAvgRating)
                .setHeader(KafkaHeaders.TOPIC, DRIVER_TOPIC)
                .setHeader("traceId", traceId)
                .build();

        kafkaTemplate.send(message);
    }

    private String getTraceId() {
        return tracer.currentTraceContext().context() != null ? Objects.requireNonNull(tracer.currentTraceContext().context()).traceId()
                : "no-trace";
    }
}
