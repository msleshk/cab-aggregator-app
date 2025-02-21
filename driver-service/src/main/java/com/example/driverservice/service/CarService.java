package com.example.driverservice.service;

import com.example.driverservice.dto.car.CarRequest;
import com.example.driverservice.dto.car.CarResponse;

import java.util.List;

public interface CarService {
    void addCar(CarRequest carDto);
    void deleteCar(Long id);
    void updateCar(Long id, CarRequest carDto);
    List<CarResponse> getAllCars();
    CarResponse getCarById(Long id);
    CarResponse getCarByCarNumber(String carNumber);
}
