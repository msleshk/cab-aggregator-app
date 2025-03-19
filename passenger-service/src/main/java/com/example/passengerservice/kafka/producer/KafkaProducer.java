package com.example.passengerservice.kafka.producer;

import com.example.passengerservice.dto.kafka.NewPassengerBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.example.passengerservice.kafka.util.Constants.PASSENGER_BALANCE_TOPIC;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNewPassengerBalance(NewPassengerBalance passengerBalanceRequest){
        kafkaTemplate.send(PASSENGER_BALANCE_TOPIC, passengerBalanceRequest);
    }
}
