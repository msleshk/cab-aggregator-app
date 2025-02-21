package com.example.driverservice.dto.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private String color;
    private String carNumber;
    private Long driverId;
}
