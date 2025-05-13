package com.example.driverservice.kafka.consumer;

import com.example.driverservice.dto.kafka.AverageRatingResponse;
import com.example.driverservice.dto.kafka.NewDriverStatus;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.model.Driver;
import com.example.driverservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;


import static com.example.driverservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final DriverRepository driverRepository;
    private final JwtDecoder jwtDecoder;

    @KafkaListener(topics = DRIVER_RATING_TOPIC, groupId = GROUP_DRIVER, containerFactory = CONTAINER_FACTORY)
    public void consumeRating(
            @Payload AverageRatingResponse ratingResponse,
            @Header(value = "Authorization", required = false) String authHeader,
            @Header(value = "traceId", required = false) String traceId) {

        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            validateToken(authHeader);

            Driver driver = findDriverById(ratingResponse.id());
            driver.setAverageRating(ratingResponse.avgRating());
            driverRepository.save(driver);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = DRIVER_STATUS_TOPIC, groupId = GROUP_DRIVER, containerFactory = CONTAINER_FACTORY)
    public void consumeStatus(
            @Payload NewDriverStatus newDriverStatus,
            @Header(value = "Authorization", required = false) String authHeader,
            @Header(value = "traceId", required = false) String traceId) {

        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            validateToken(authHeader);

            Driver driver = findDriverById(newDriverStatus.driverId());
            driver.setDriverStatus(newDriverStatus.status());
            driverRepository.save(driver);
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

    private Driver findDriverById(Long id) {
        return driverRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found!"));
    }
}
