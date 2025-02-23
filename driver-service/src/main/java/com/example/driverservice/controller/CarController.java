package com.example.driverservice.controller;

import com.example.driverservice.dto.car.CarRequest;
import com.example.driverservice.dto.car.CarResponse;
import com.example.driverservice.exception.ValidationException;
import com.example.driverservice.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(carService.getCarById(id));
    }

    @GetMapping("/car-by-number/{carNumber}")
    public ResponseEntity<CarResponse> getCarByNumber(@PathVariable("carNumber") String carNumber) {
        return ResponseEntity.ok().body(carService.getCarByCarNumber(carNumber));
    }

    @GetMapping()
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok().body(carService.getAllCars());
    }

    @PostMapping
    public ResponseEntity<Void> addCar(@RequestBody @Valid CarRequest carRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        carService.addCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCar(@PathVariable("id") Long id, @RequestBody @Valid CarRequest carToUpdate, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        carService.updateCar(id, carToUpdate);
        return ResponseEntity.ok().build();
    }
}
