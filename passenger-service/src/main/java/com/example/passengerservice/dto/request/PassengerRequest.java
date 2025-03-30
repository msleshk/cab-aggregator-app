package com.example.passengerservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record PassengerRequest(
        @NotEmpty(message = "Name should not be empty!")
        String name,
        @NotEmpty(message = "Email should not be empty!")
        @Email(message = "Email should be correct!")
        String email,
        @NotEmpty(message = "Phone number should not be empty!")
        @Pattern(regexp = "\\+375\\((29|33|44|25)\\)\\d{3}-\\d{2}-\\d{2}", message = "Phone number should follow pattern: +000(00)000-00-00!")
        String phoneNumber
) {
}
