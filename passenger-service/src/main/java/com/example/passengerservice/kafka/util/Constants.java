package com.example.passengerservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final int PARTITION_COUNT = 3;
    public static final String PASSENGER_TOPIC = "passenger-rating-topic";
    public static final String PASSENGER_BALANCE_TOPIC = "passenger-balance-topic";
    public static final String GROUP_PASSENGER = "passenger-group";
    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String TRUSTED_PACKAGES = "com.example.passengerservice.dto.kafka";
    public static final String DESERIALIZE_AVG_RATING = "cab.app.ratingservice.dto.response.AverageRating" +
            ":com.example.passengerservice.dto.kafka.AverageRatingResponse";
}
