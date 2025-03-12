package com.example.driverservice.dto.response;

import com.example.driverservice.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponse {
    private Long id;
    private String name;
    private String email;
    private Double averageRating;
    private String phoneNumber;
    private Gender gender;
    private Long carId;
}
