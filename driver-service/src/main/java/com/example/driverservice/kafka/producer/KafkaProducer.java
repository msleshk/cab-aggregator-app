package com.example.driverservice.kafka.producer;

import com.example.driverservice.dto.kafka.NewDriverBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.example.driverservice.kafka.util.Constants.DRIVER_BALANCE_TOPIC;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNewDriverBalance(NewDriverBalance driverBalance) {
        kafkaTemplate.send(DRIVER_BALANCE_TOPIC, driverBalance);
    }
}
