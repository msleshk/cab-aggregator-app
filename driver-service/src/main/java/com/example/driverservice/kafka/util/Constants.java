package com.example.driverservice.kafka.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final int PARTITION_COUNT = 3;
    public static final String DRIVER_RATING_TOPIC = "driver-rating-topic";
    public static final String DRIVER_BALANCE_TOPIC = "driver-balance-topic";
    public static final String DRIVER_STATUS_TOPIC = "driver-status-update";
    public static final String GROUP_DRIVER = "driver-group";
    public static final String CONTAINER_FACTORY = "kafkaListenerContainerFactory";
    public static final String TRUSTED_PACKAGES = "com.example.driverservice.dto.kafka, cab.app.rideservice.dto.kafka";
    public static final String DESERIALIZE_AVG_RATING = "cab.app.ratingservice.dto.response.AverageRating" +
            ":com.example.driverservice.dto.kafka.AverageRatingResponse";
    public static final String DESERIALIZE_NEW_DRIVER_STATUS = "cab.app.rideservice.dto.kafka.UpdateDriverStatus" +
            ":com.example.driverservice.dto.kafka.NewDriverStatus";
    public static final String DRIVER_DESERIALIZE_TYPE_MAPPINGS = DESERIALIZE_AVG_RATING + "," + DESERIALIZE_NEW_DRIVER_STATUS;
}
