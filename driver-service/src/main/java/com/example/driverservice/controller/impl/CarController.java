package com.example.driverservice.controller.impl;

import com.example.driverservice.controller.CarApi;
import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.dto.response.CarResponse;
import com.example.driverservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CarController implements CarApi {

    private final CarService carService;

    @Override
    public ResponseEntity<CarResponse> getCarById(Long id) {
        return ResponseEntity.ok().body(carService.getCarById(id));
    }

    @Override
    public ResponseEntity<CarResponse> getCarByNumber(String carNumber) {
        return ResponseEntity.ok().body(carService.getCarByCarNumber(carNumber));
    }

    @Override
    public ResponseEntity<ResponseList<CarResponse>> getAllCars(int offset, int limit) {
        return ResponseEntity.ok().body(carService.getAllCars(offset, limit));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<Void> addCar(CarRequest carRequest) {
        carService.addCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<Void> deleteCar(Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<Void> updateCar(Long id, CarRequest carToUpdate) {
        carService.updateCar(id, carToUpdate);
        return ResponseEntity.ok().build();
    }
}
