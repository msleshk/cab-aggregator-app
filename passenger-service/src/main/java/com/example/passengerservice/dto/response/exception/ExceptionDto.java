package com.example.passengerservice.dto.response;

import lombok.Builder;

@Builder
public record ExceptionDto(String message) {
}
