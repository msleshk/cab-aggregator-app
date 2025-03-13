package com.example.driverservice.controller;

import com.example.driverservice.dto.request.RequestParams;
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
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriver(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(driverService.getDriverById(id));
    }

    @GetMapping
    public ResponseEntity<ResponseList<DriverResponse>> getAllDrivers(@RequestBody @Valid RequestParams requestParams) {
        return ResponseEntity.ok().body(driverService.getAllDrivers(requestParams.offset(), requestParams.limit()));
    }

    @PostMapping
    public ResponseEntity<Void> addDriver(@RequestBody @Valid DriverRequest driverRequest) {
        driverService.addDriver(driverRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateDriver(@PathVariable("id") Long id, @RequestBody @Valid DriverRequest driverToUpdate) {
        driverService.updateDriver(id, driverToUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable("id") Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.ok().build();
    }
}
