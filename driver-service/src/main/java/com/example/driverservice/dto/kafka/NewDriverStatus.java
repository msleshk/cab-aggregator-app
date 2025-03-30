package com.example.driverservice.dto.kafka;

import com.example.driverservice.model.enums.DriverStatus;
import lombok.Builder;

@Builder
public record NewDriverStatus(
        Long driverId,
        DriverStatus status
) {
}
