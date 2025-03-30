package com.example.driverservice.service.implementation;

import com.example.driverservice.dto.kafka.NewDriverBalance;
import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import com.example.driverservice.kafka.producer.KafkaProducer;
import com.example.driverservice.model.Car;
import com.example.driverservice.model.Driver;
import com.example.driverservice.model.enums.DriverStatus;
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
    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public void addDriver(DriverRequest driverDto) {
        checkIfDriverUnique(driverDto);
        if (driverDto.carId() != null) {
            checkIfCarTaken(driverDto.carId());
        }
        Driver newDriver = driverMapper.toEntity(driverDto);
        newDriver.setDriverStatus(DriverStatus.AVAILABLE);
        driverRepository.save(newDriver);
//        kafkaProducer.sendNewDriverBalance(NewDriverBalance.builder()
//                .id(newDriver.getId())
//                .build());
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

        driverToUpdate.setName(driverDto.name());
        driverToUpdate.setEmail(driverDto.email());
        driverToUpdate.setPhoneNumber(driverDto.phoneNumber());
        driverToUpdate.setGender(driverDto.gender());

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
        if (!driverToUpdate.getEmail().equals(driverDto.email())) {
            checkIfEmailUnique(driverDto.email());
        }
        if (!driverToUpdate.getPhoneNumber().equals(driverDto.phoneNumber())) {
            checkIfPhoneUnique(driverDto.phoneNumber());
        }
        if (driverDto.carId() == null) {
            driverToUpdate.setCar(null);
        } else {
            checkIfCarTaken(driverDto.carId());
            driverToUpdate.setCar(findCarById(driverDto.carId()));
        }
    }

    private void checkIfDriverUnique(DriverRequest driverRequest) {
        checkIfEmailUnique(driverRequest.email());
        checkIfPhoneUnique(driverRequest.phoneNumber());
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
