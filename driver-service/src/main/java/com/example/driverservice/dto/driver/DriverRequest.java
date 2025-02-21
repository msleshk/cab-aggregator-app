package com.example.driverservice.dto.driver;

import com.example.driverservice.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private Long carId;
}
