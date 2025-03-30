package com.example.driverservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CarRequest (
        @NotNull(message = "Brand cannot be empty")
        @Size(max = 50, message = "Brand must not exceed 50 characters")
        String brand,

        @NotNull(message = "Model cannot be empty")
        @Size(max = 50, message = "Model must not exceed 50 characters")
        String model,

        @NotNull(message = "Color cannot be empty")
        @Size(max = 30, message = "Color must not exceed 30 characters")
        String color,

        @NotNull(message = "Car number cannot be empty")
        @Size(max = 20, message = "Car number must not exceed 20 characters")
        @Pattern(regexp = "\\d{4}[A-Z]{2}-\\d", message = "Car number must contain only uppercase letters, numbers, and hyphens")
        String carNumber
) {
}
