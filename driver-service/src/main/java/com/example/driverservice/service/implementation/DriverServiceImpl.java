package com.example.driverservice.service.implementation;

import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import com.example.driverservice.model.Car;
import com.example.driverservice.model.Driver;
import com.example.driverservice.repository.CarRepository;
import com.example.driverservice.repository.DriverRepository;
import com.example.driverservice.service.DriverService;
import com.example.driverservice.util.DriverMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final CarRepository carRepository;
    private final DriverMapper driverMapper;

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
        Driver driver = findDriverById(id);
        driver.setCar(null);
        driver.setDeleted(true);
        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void updateDriver(Long id, DriverRequest driverDto) {
        Driver driverToUpdate = findDriverById(id);

        updateIfChanged(driverToUpdate, driverDto);

        driverToUpdate.setName(driverDto.getName());
        driverToUpdate.setEmail(driverDto.getEmail());
        driverToUpdate.setPhoneNumber(driverDto.getPhoneNumber());
        driverToUpdate.setGender(driverDto.getGender());

        driverRepository.save(driverToUpdate);
    }

    @Override
    public ResponseList<DriverResponse> getAllDrivers(int offset, int limit) {
        return new ResponseList<>(
                driverRepository.findAllByDeletedFalse(PageRequest.of(offset, limit))
                        .getContent().stream()
                        .map(driverMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public DriverResponse getDriverById(Long id) {
        return driverMapper.toDto(findDriverById(id));
    }

    private void updateIfChanged(Driver driverToUpdate, DriverRequest driverDto){
        if (!driverToUpdate.getEmail().equals(driverDto.getEmail())) {
            checkIfEmailUnique(driverDto.getEmail());
        }
        if (!driverToUpdate.getPhoneNumber().equals(driverDto.getPhoneNumber())) {
            checkIfPhoneUnique(driverDto.getPhoneNumber());
        }
        if (driverDto.getCarId() == null) {
            driverToUpdate.setCar(null);
        } else {
            checkIfCarTaken(driverDto.getCarId());
            driverToUpdate.setCar(findCarById(driverDto.getCarId()));
        }
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

    private Driver findDriverById(Long driverId) {
        return driverRepository.findByIdAndDeletedFalse(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver with this id not found!"));
    }

    private void checkIfCarTaken(Long carId) {
        Car car = findCarById(carId);
        if (car.getDriver() != null) {
            throw new ResourceAlreadyTakenException("This car already have owner!");
        }
    }
}
