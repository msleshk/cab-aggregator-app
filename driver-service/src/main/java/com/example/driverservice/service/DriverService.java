package com.example.driverservice.service;

import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;

public interface DriverService {
    void addDriver(DriverRequest driverDto);

    void deleteDriver(Long id);

    void updateDriver(Long id, DriverRequest driverDto);

    ResponseList<DriverResponse> getAllDrivers(int offset, int limit);

    DriverResponse getDriverById(Long id);
}
