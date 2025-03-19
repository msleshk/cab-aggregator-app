package com.example.passengerservice.dto.kafka;

import lombok.Builder;

@Builder
public record NewPassengerBalance(
        Long id
) {
}
