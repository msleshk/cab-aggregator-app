package com.example.driverservice.controller.impl;

import com.example.driverservice.controller.DriverApi;
import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DriverController implements DriverApi {

    private final DriverService driverService;

    @Override
    public ResponseEntity<DriverResponse> getDriver(Long id) {
        return ResponseEntity.ok().body(driverService.getDriverById(id));
    }

    @Override
    public ResponseEntity<ResponseList<DriverResponse>> getAllDrivers(int offset, int limit) {
        return ResponseEntity.ok().body(driverService.getAllDrivers(offset, limit));
    }

    @Override
    public ResponseEntity<Void> addDriver(DriverRequest driverRequest) {
        driverService.addDriver(driverRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> updateDriver(Long id, DriverRequest driverToUpdate) {
        driverService.updateDriver(id, driverToUpdate);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteDriver(Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.ok().build();
    }
}
