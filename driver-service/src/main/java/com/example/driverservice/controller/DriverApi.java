package com.example.driverservice.controller;

import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.dto.response.ResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Drivers", description = "API for drivers")
@RequestMapping("/api/v1/drivers")
public interface DriverApi {

    @Operation(summary = "Get driver by id ID")
    @GetMapping("/{id}")
    ResponseEntity<DriverResponse> getDriver(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers);

    @Operation(summary = "Get list of all drivers")
    @GetMapping
    ResponseEntity<ResponseList<DriverResponse>> getAllDrivers(@RequestParam
                                                               @Min(value = 0, message = "Offset should be greater than 0!")
                                                               int offset,

                                                               @RequestParam
                                                               @Min(value = 1, message = "Limit should be greater than 1")
                                                               @Max(value = 100, message = "Limit should be less than 100")
                                                               int limit);

    @Operation(summary = "Add new driver")
    @PostMapping
    ResponseEntity<Void> addDriver(@RequestBody @Valid DriverRequest driverRequest);

    @Operation(summary = "Update driver")
    @PatchMapping("/{id}")
    ResponseEntity<Void> updateDriver(@PathVariable("id") Long id, @RequestBody @Valid DriverRequest driverToUpdate);

    @Operation(summary = "Delete driver by ID")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteDriver(@PathVariable("id") Long id);
}
