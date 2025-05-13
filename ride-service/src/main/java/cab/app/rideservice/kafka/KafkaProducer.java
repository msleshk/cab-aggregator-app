package cab.app.rideservice.kafka;

import cab.app.rideservice.dto.kafka.CreatePayment;
import cab.app.rideservice.dto.kafka.UpdateDriverStatus;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static cab.app.rideservice.kafka.util.Constants.DRIVER_STATUS_TOPIC;
import static cab.app.rideservice.kafka.util.Constants.PAYMENT_TOPIC;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Tracer tracer;

    public void sendNewPayment(CreatePayment payment) {
        String traceId = getTraceId();

        Message<CreatePayment> message = MessageBuilder
                .withPayload(payment)
                .setHeader("Authorization", "Bearer " + getJwtToken())
                .setHeader(KafkaHeaders.TOPIC, PAYMENT_TOPIC)
                .setHeader("traceId", traceId)
                .build();

        kafkaTemplate.send(message);
    }

    public void sendDriverStatusUpdate(UpdateDriverStatus driverStatusUpdate){
        String traceId = getTraceId();

        Message<UpdateDriverStatus> message = MessageBuilder
                .withPayload(driverStatusUpdate)
                .setHeader("Authorization", "Bearer " + getJwtToken())
                .setHeader(KafkaHeaders.TOPIC, DRIVER_STATUS_TOPIC)
                .setHeader("traceId", traceId)
                .build();

        kafkaTemplate.send(message);
    }

    private String getJwtToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtAuthenticationToken) auth).getToken().getTokenValue();
    }

    private String getTraceId() {
        return tracer.currentTraceContext().context() != null ? Objects.requireNonNull(tracer.currentTraceContext().context()).traceId()
                : "no-trace";
    }
}
