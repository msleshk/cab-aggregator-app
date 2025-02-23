package com.example.driverservice.service.implementation;

import com.example.driverservice.dto.driver.DriverRequest;
import com.example.driverservice.dto.driver.DriverResponse;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import com.example.driverservice.model.Car;
import com.example.driverservice.model.Driver;
import com.example.driverservice.repository.CarRepository;
import com.example.driverservice.repository.DriverRepository;
import com.example.driverservice.service.DriverService;
import com.example.driverservice.util.DriverMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final CarRepository carRepository;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverRepository driverRepository, CarRepository carRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.carRepository = carRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    @Transactional
    public void addDriver(DriverRequest driverDto) {
        checkIfDriverUnique(driverDto);
        if (driverDto.getCarId() != null) {
            checkIfCarTaken(driverDto.getCarId());
        }
        driverRepository.save(driverMapper.toEntity(driverDto));
    }

    @Override
    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = driverRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver with this id not found!"));
        driver.setCar(null);
        driver.setDeleted(true);
        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void updateDriver(Long id, DriverRequest driverDto) {
        Driver driverToUpdate = driverRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver with this id not found!"));
        if (!driverToUpdate.getEmail().equals(driverDto.getEmail())) {
            checkIfEmailUnique(driverDto.getEmail());
        }
        if (!driverToUpdate.getPhoneNumber().equals(driverDto.getPhoneNumber())) {
            checkIfPhoneUnique(driverDto.getPhoneNumber());
        }
        checkIfCarTaken(driverDto.getCarId());

        driverToUpdate.setName(driverDto.getName());
        driverToUpdate.setEmail(driverDto.getEmail());
        driverToUpdate.setPhoneNumber(driverDto.getPhoneNumber());
        driverToUpdate.setGender(driverDto.getGender());
        driverToUpdate.setCar(findCarById(driverDto.getCarId()));
    }

    @Override
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAllByDeletedFalse().stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse getDriverById(Long id) {
        return driverRepository.findByIdAndDeletedFalse(id).map(driverMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Driver with this id not found!"));
    }

    private void checkIfDriverUnique(DriverRequest driverRequest) {
        checkIfEmailUnique(driverRequest.getEmail());
        checkIfPhoneUnique(driverRequest.getPhoneNumber());
    }

    private void checkIfPhoneUnique(String phoneNumber) {
        if (driverRepository.findDriverByPhoneNumberAndDeletedFalse(phoneNumber).isPresent()) {
            throw new ResourceAlreadyTakenException("This phone number already taken!");
        }
    }

    private void checkIfEmailUnique(String email) {
        if (driverRepository.findDriverByEmailAndDeletedFalse(email).isPresent()) {
            throw new ResourceAlreadyTakenException("This email already taken!");
        }
    }

    private Car findCarById(Long carId) {
        return carRepository.findCarByIdAndDeletedFalse(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car with this id not found!"));
    }

    private void checkIfCarTaken(Long carId) {
        Car car = findCarById(carId);
        if (car.getDriver() != null) {
            throw new ResourceAlreadyTakenException("This car already have owner!");
        }
    }
}
