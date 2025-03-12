package com.example.driverservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String DRIVER_TOPIC = "driver-topic";
    public static final String GROUP_DRIVER = "driver-group";
    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String TRUSTED_PACKAGES = "com.example.driverservice.dto.kafka";
    public static final String DESERIALIZE_AVG_RATING = "cab.app.ratingservice.dto.response.AverageRating" +
            ":com.example.driverservice.dto.kafka.AverageRatingResponse";
}
