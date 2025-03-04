package com.example.driverservice.service;

import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.dto.response.CarResponse;

public interface CarService {
    void addCar(CarRequest carDto);

    void deleteCar(Long id);

    void updateCar(Long id, CarRequest carDto);

    ResponseList<CarResponse> getAllCars(int offset, int limit);

    CarResponse getCarById(Long id);

    CarResponse getCarByCarNumber(String carNumber);
}
