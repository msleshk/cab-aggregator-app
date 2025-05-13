package com.example.driverservice.controller;

import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.dto.response.CarResponse;
import com.example.driverservice.dto.response.ResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Car API", description = "API for cars")
@RequestMapping("/api/v1/cars")
public interface CarApi {

    @Operation(summary = "Get car by ID")
    @GetMapping("/{id}")
    ResponseEntity<CarResponse> getCarById(@PathVariable("id") Long id);

    @Operation(summary = "Get car by car number")
    @GetMapping("/car-by-number/{carNumber}")
    ResponseEntity<CarResponse> getCarByNumber(@PathVariable("carNumber") String carNumber);

    @Operation(summary = "Ger all cars")
    @GetMapping
    ResponseEntity<ResponseList<CarResponse>> getAllCars(@RequestParam
                                                         @Min(value = 0, message = "Offset should be greater than 0!")
                                                         int offset,

                                                         @RequestParam
                                                         @Min(value = 1, message = "Limit should be greater than 1")
                                                         @Max(value = 100, message = "Limit should be less than 100")
                                                         int limit);

    @Operation(summary = "Add new car")
    @PostMapping
    ResponseEntity<Void> addCar(@RequestBody @Valid CarRequest carRequest);

    @Operation(summary = "Delete car by id")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCar(@PathVariable("id") Long id);

    @Operation(summary = "Update car")
    @PatchMapping("/{id}")
    ResponseEntity<Void> updateCar(@PathVariable("id") Long id, @RequestBody @Valid CarRequest carToUpdate);
}
