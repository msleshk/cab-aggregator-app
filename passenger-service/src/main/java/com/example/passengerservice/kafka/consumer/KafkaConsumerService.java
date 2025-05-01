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
import org.springframework.stereotype.Component;

import static com.example.passengerservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PassengerRepository passengerRepository;

    @KafkaListener(topics = PASSENGER_TOPIC, groupId = GROUP_PASSENGER, containerFactory = CONTAINER_FACTORY)
    public void consumeRating(@Payload AverageRatingResponse ratingResponse,
                              @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            Passenger passenger = findPassengerById(ratingResponse.id());
            passenger.setAverageRating(ratingResponse.avgRating());
            passengerRepository.save(passenger);
        } finally {
            MDC.clear();
        }
    }

    private Passenger findPassengerById(Long id) {
        return passengerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new PassengerNotFoundException("No such passenger!"));
    }

}