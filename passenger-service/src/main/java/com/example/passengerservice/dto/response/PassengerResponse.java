package com.example.passengerservice.dto.response;


public record PassengerResponse(
        Long id,
        String name,
        String email,
        String phoneNumber
) {
}
