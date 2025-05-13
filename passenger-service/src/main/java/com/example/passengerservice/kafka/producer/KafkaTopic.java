package com.example.passengerservice.kafka.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.example.passengerservice.kafka.util.Constants.PARTITION_COUNT;
import static com.example.passengerservice.kafka.util.Constants.PASSENGER_BALANCE_TOPIC;

@Configuration
public class KafkaTopic {

    @Bean
    public NewTopic newPassengerBalanceTopic() {
        return TopicBuilder
                .name(PASSENGER_BALANCE_TOPIC)
                .partitions(PARTITION_COUNT)
                .build();
    }
}
