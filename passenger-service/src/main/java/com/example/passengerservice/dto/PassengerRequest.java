package com.example.passengerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequest {
    @NotEmpty(message = "Name should not be empty!")
    private String name;
    @NotEmpty(message = "Email should not be empty!")
    @Email(message = "Email should be correct!")
    private String email;
    @NotEmpty(message = "Phone number should not be empty!")
    @Pattern(regexp = "\\+\\d{3}\\(\\d{2}\\)\\d{3}-\\d{2}-\\d{2}", message = "Phone number should follow pattern: +000(00)000-00-00!")
    private String phoneNumber;
}
