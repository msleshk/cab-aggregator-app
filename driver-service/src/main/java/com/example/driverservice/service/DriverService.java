package com.example.driverservice.service;

import com.example.driverservice.dto.driver.DriverRequest;
import com.example.driverservice.dto.driver.DriverResponse;

import java.util.List;

public interface DriverService {
    void addDriver(DriverRequest driverDto);

    void deleteDriver(Long id);

    void updateDriver(Long id, DriverRequest driverDto);

    List<DriverResponse> getAllDrivers();

    DriverResponse getDriverById(Long id);
}
