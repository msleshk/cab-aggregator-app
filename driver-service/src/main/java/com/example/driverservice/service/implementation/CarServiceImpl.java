package com.example.driverservice.service.implementation;

import com.example.driverservice.dto.car.CarRequest;
import com.example.driverservice.dto.car.CarResponse;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import com.example.driverservice.model.Car;
import com.example.driverservice.model.Driver;
import com.example.driverservice.repository.CarRepository;
import com.example.driverservice.repository.DriverRepository;
import com.example.driverservice.service.CarService;
import com.example.driverservice.util.CarMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository, DriverRepository driverRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.driverRepository = driverRepository;
        this.carMapper = carMapper;
    }

    @Override
    @Transactional
    public void addCar(CarRequest carDto) {
        checkIfCarUnique(carDto);
        carRepository.save(carMapper.toEntity(carDto));
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        Car carToDelete = carRepository.findCarByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id not found!"));
        if (carToDelete.getDriver() != null) {
            Driver driver = carToDelete.getDriver();
            driver.setCar(null);
            driverRepository.save(driver);
        }        carToDelete.setDeleted(true);
        carRepository.save(carToDelete);
    }

    @Override
    @Transactional
    public void updateCar(Long id, CarRequest carDto) {
        Car carToUpdate = carRepository.findCarByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id not found!"));
        if (!carToUpdate.getCarNumber().equals(carDto.getCarNumber())) {
            checkIfCarUnique(carDto);
        }
        carToUpdate.setCarNumber(carDto.getCarNumber());
        carToUpdate.setBrand(carDto.getBrand());
        carToUpdate.setModel(carDto.getModel());
        carToUpdate.setColor(carDto.getColor());

        carRepository.save(carToUpdate);
    }

    @Override
    public List<CarResponse> getAllCars() {
        return carRepository.findAllByDeletedFalse().stream()
                .map(carMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CarResponse getCarById(Long id) {
        return carMapper.toDto(carRepository.findCarByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id not found!")));
    }

    @Override
    public CarResponse getCarByCarNumber(String carNumber) {
        return carMapper.toDto(carRepository.findCarByCarNumberAndDeletedFalse(carNumber)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id not found!")));
    }

    private void checkIfCarUnique(CarRequest carRequest) {
        if (carRepository.findCarByCarNumberAndDeletedFalse(carRequest.getCarNumber()).isPresent()) {
            throw new ResourceAlreadyTakenException("This car number already taken!");
        }
    }
}
