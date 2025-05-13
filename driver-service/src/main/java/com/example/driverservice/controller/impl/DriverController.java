package com.example.driverservice.controller.impl;

import com.example.driverservice.controller.DriverApi;
import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DriverController implements DriverApi {

    private final DriverService driverService;

    @Override
    public ResponseEntity<DriverResponse> getDriver(Long id, @RequestHeader HttpHeaders headers) {
        log.info("Getting driver with id={}", id);
        return ResponseEntity.ok().body(driverService.getDriverById(id));
    }

    @Override
    public ResponseEntity<ResponseList<DriverResponse>> getAllDrivers(int offset, int limit) {
        log.info("Getting all drivers");
        return ResponseEntity.ok().body(driverService.getAllDrivers(offset, limit));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<Void> addDriver(DriverRequest driverRequest) {
        log.info("Adding driver with name={}", driverRequest.name());
        driverService.addDriver(driverRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<Void> updateDriver(Long id, DriverRequest driverToUpdate) {
        log.info("Updating driver with id={}", id);
        driverService.updateDriver(id, driverToUpdate);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<Void> deleteDriver(Long id) {
        log.info("Deleting driver with id={}", id);
        driverService.deleteDriver(id);
        return ResponseEntity.ok().build();
    }
}
