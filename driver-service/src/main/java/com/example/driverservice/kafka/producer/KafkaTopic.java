package com.example.driverservice.kafka.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.example.driverservice.kafka.util.Constants.DRIVER_BALANCE_TOPIC;
import static com.example.driverservice.kafka.util.Constants.PARTITION_COUNT;

@Configuration
public class KafkaTopic {
    @Bean
    public NewTopic newDriverBalanceTopic() {
        return TopicBuilder
                .name(DRIVER_BALANCE_TOPIC)
                .partitions(PARTITION_COUNT)
                .build();
    }
}
