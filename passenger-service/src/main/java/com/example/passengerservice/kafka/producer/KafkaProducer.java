package com.example.passengerservice.kafka.producer;

import com.example.passengerservice.dto.kafka.NewPassengerBalance;
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

import static com.example.passengerservice.kafka.util.Constants.PASSENGER_BALANCE_TOPIC;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final Tracer tracer;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNewPassengerBalance(NewPassengerBalance passengerBalanceRequest){
        String traceId = getTraceId();

        Message<NewPassengerBalance> message = MessageBuilder
                .withPayload(passengerBalanceRequest)
                .setHeader("Authorization", "Bearer " + getJwtToken())
                .setHeader(KafkaHeaders.TOPIC, PASSENGER_BALANCE_TOPIC)
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
