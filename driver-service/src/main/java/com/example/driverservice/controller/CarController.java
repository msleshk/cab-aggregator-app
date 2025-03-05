package com.example.driverservice.controller;

import com.example.driverservice.dto.request.RequestParams;
import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.dto.response.CarResponse;
import com.example.driverservice.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(carService.getCarById(id));
    }

    @GetMapping("/car-by-number/{carNumber}")
    public ResponseEntity<CarResponse> getCarByNumber(@PathVariable("carNumber") String carNumber) {
        return ResponseEntity.ok().body(carService.getCarByCarNumber(carNumber));
    }

    @GetMapping()
    public ResponseEntity<ResponseList<CarResponse>> getAllCars(@RequestBody @Valid RequestParams requestParams) {
        return ResponseEntity.ok().body(carService.getAllCars(requestParams.offset(), requestParams.limit()));
    }

    @PostMapping
    public ResponseEntity<Void> addCar(@RequestBody @Valid CarRequest carRequest) {
        carService.addCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCar(@PathVariable("id") Long id, @RequestBody @Valid CarRequest carToUpdate) {
        carService.updateCar(id, carToUpdate);
        return ResponseEntity.ok().build();
    }
}
