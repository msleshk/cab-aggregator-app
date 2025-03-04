package com.example.driverservice.dto.request;

import com.example.driverservice.model.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {
    @NotNull(message = "Name cannot be empty")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotNull(message = "Phone number cannot be empty")
    @Size(max = 255, message = "Phone number must not exceed 255 characters")
    @Pattern(regexp = "\\+375\\((29|33|44|25)\\)\\d{3}-\\d{2}-\\d{2}", message = "Phone number should follow pattern: +375(29/33/44/25)000-00-00!")
    private String phoneNumber;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    private Long carId;
}
