package com.example.driverservice.kafka.producer;

import com.example.driverservice.dto.kafka.NewDriverBalance;
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

import static com.example.driverservice.kafka.util.Constants.DRIVER_BALANCE_TOPIC;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final Tracer tracer;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNewDriverBalance(NewDriverBalance driverBalance) {
        String traceId = getTraceId();

        Message<NewDriverBalance> message = MessageBuilder
                .withPayload(driverBalance)
                .setHeader("Authorization", "Bearer " + getJwtToken())
                .setHeader(KafkaHeaders.TOPIC, DRIVER_BALANCE_TOPIC)
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
