package com.example.passengerservice.kafka;

import com.example.passengerservice.dto.kafka.AverageRatingResponse;
import com.example.passengerservice.exception.PassengerNotFoundException;
import com.example.passengerservice.model.Passenger;
import com.example.passengerservice.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.passengerservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PassengerRepository passengerRepository;

    @KafkaListener(topics = PASSENGER_TOPIC, groupId = GROUP_PASSENGER, containerFactory = CONTAINER_FACTORY)
    public void consume(AverageRatingResponse ratingResponse){
        Passenger passenger = findPassengerById(ratingResponse.id());
        passenger.setAverageRating(ratingResponse.avgRating());
        passengerRepository.save(passenger);
        System.out.println("Passenger updated id, avg rating:" + passenger.getId() + passenger.getAverageRating() ); //todo delete later
    }

    private Passenger findPassengerById(Long id){
        return passengerRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new PassengerNotFoundException("No such passenger!"));
    }
}
