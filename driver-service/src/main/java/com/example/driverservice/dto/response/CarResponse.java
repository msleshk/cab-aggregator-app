package com.example.driverservice.dto.response;


public record CarResponse(
        Long id,
        String brand,
        String model,
        String color,
        String carNumber,
        Long driverId
) {
}
