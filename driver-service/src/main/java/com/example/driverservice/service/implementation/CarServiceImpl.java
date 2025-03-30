package com.example.driverservice.service.implementation;

import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.dto.response.CarResponse;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import com.example.driverservice.model.Car;
import com.example.driverservice.model.Driver;
import com.example.driverservice.repository.CarRepository;
import com.example.driverservice.repository.DriverRepository;
import com.example.driverservice.service.CarService;
import com.example.driverservice.util.CarMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final CarMapper carMapper;

    @Override
    @Transactional
    public void addCar(CarRequest carDto) {
        checkIfCarUnique(carDto);
        carRepository.save(carMapper.toEntity(carDto));
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        Car carToDelete = findCarById(id);
        if (carToDelete.getDriver() != null) {
            Driver driver = carToDelete.getDriver();
            driver.setCar(null);
            driverRepository.save(driver);
        }
        carToDelete.setDeleted(true);
        carRepository.save(carToDelete);
    }

    @Override
    @Transactional
    public void updateCar(Long id, CarRequest carDto) {
        Car carToUpdate = findCarById(id);

        updateIfChanged(carToUpdate, carDto);

        carToUpdate.setCarNumber(carDto.carNumber());
        carToUpdate.setBrand(carDto.brand());
        carToUpdate.setModel(carDto.model());
        carToUpdate.setColor(carDto.color());

        carRepository.save(carToUpdate);
    }

    @Override
    public ResponseList<CarResponse> getAllCars(int offset, int limit) {
        Page<Car> carPage = carRepository.findAllByDeletedFalse(PageRequest.of(offset, limit));

        List<CarResponse> carList = carPage.getContent().stream()
                .map(carMapper::toDto)
                .toList();

        return new ResponseList<>(carList);
    }

    @Override
    public CarResponse getCarById(Long id) {
        return carMapper.toDto(findCarById(id));
    }

    @Override
    public CarResponse getCarByCarNumber(String carNumber) {
        return carMapper.toDto(carRepository.findCarByCarNumberAndDeletedFalse(carNumber)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id not found!")));
    }

    private void updateIfChanged(Car carToUpdate, CarRequest carDto){
        if (!carToUpdate.getCarNumber().equals(carDto.carNumber())) {
            checkIfCarUnique(carDto);
        }
    }

    private void checkIfCarUnique(CarRequest carRequest) {
        if (carRepository.findCarByCarNumberAndDeletedFalse(carRequest.carNumber()).isPresent()) {
            throw new ResourceAlreadyTakenException("This car number already taken!");
        }
    }

    private Car findCarById(Long carId) {
        return carRepository.findCarByIdAndDeletedFalse(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id not found!"));
    }
}
