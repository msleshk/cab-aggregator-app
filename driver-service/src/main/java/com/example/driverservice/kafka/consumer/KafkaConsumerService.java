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
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;


import static com.example.driverservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final DriverRepository driverRepository;

    @KafkaListener(topics = DRIVER_RATING_TOPIC, groupId = GROUP_DRIVER, containerFactory = CONTAINER_FACTORY)
    public void consumeRating(
            @Payload AverageRatingResponse ratingResponse,
            @Header(value = "traceId", required = false) String traceId) {

        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
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
            @Header(value = "traceId", required = false) String traceId) {

        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            Driver driver = findDriverById(newDriverStatus.driverId());
            driver.setDriverStatus(newDriverStatus.status());
            driverRepository.save(driver);
        } finally {
            MDC.clear();
        }
    }

    private Driver findDriverById(Long id) {
        return driverRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found!"));
    }
}
