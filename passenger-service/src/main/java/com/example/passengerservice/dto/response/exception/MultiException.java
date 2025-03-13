package com.example.passengerservice.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record MultiException(String message, Map<String, String> errors) {
}
