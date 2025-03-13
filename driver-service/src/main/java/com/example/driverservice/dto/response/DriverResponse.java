package com.example.driverservice.dto.response;

import com.example.driverservice.model.enums.Gender;

public record DriverResponse(
        Long id,
        String name,
        String email,
        Double averageRating,
        String phoneNumber,
        Gender gender,
        Long carId
) {
}
