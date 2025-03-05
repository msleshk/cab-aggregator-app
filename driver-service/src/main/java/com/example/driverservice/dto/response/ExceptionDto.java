package com.example.driverservice.dto.response;

import lombok.Builder;

@Builder
public record ExceptionDto(String message) {
}
