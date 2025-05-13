package com.example.passengerservice.kafka.consumer;

import com.example.passengerservice.dto.kafka.AverageRatingResponse;
import com.example.passengerservice.exception.PassengerNotFoundException;
import com.example.passengerservice.model.Passenger;
import com.example.passengerservice.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import static com.example.passengerservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PassengerRepository passengerRepository;
    private final JwtDecoder jwtDecoder;

    @KafkaListener(topics = PASSENGER_TOPIC, groupId = GROUP_PASSENGER, containerFactory = CONTAINER_FACTORY)
    public void consumeRating(@Payload AverageRatingResponse ratingResponse,
                              @Header(value = "Authorization", required = false) String authHeader,
                              @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            validateToken(authHeader);

            Passenger passenger = findPassengerById(ratingResponse.id());
            passenger.setAverageRating(ratingResponse.avgRating());
            passengerRepository.save(passenger);
        } finally {
            MDC.clear();
        }
    }

    private void validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Jwt jwt = jwtDecoder.decode(token);

        if (!jwt.getClaimAsStringList("roles").contains("YOUR_EXPECTED_ROLE")) {
            throw new AccessDeniedException("Invalid role");
        }
    }

    private Passenger findPassengerById(Long id) {
        return passengerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new PassengerNotFoundException("No such passenger!"));
    }

}