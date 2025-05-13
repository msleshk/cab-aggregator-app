package com.example.driverservice.service;

import com.example.driverservice.dto.kafka.NewDriverBalance;
import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import com.example.driverservice.kafka.producer.KafkaProducer;
import com.example.driverservice.model.Car;
import com.example.driverservice.model.Driver;
import com.example.driverservice.model.enums.DriverStatus;
import com.example.driverservice.model.enums.Gender;
import com.example.driverservice.repository.CarRepository;
import com.example.driverservice.repository.DriverRepository;
import com.example.driverservice.service.implementation.DriverServiceImpl;
import com.example.driverservice.util.DriverMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.driverservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {
    @Mock
    private DriverRepository driverRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private DriverMapper driverMapper;
    @Mock
    private KafkaProducer kafkaProducer;
    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;
    private Car car;
    private DriverRequest driverRequest;
    private DriverResponse driverResponse;

    @BeforeEach
    void setUp() {
        driver = new Driver();
        driver.setId(DRIVER_ID);
        driver.setName(DRIVER_NAME);
        driver.setEmail(DRIVER_EMAIL);
        driver.setPhoneNumber(DRIVER_PHONE);
        driver.setDriverStatus(DriverStatus.AVAILABLE);
        driver.setGender(Gender.MALE);

        car = new Car();
        car.setId(CAR_ID);
        car.setBrand(BRAND);
        car.setModel(MODEL);
        car.setColor(COLOR);
        car.setCarNumber(CAR_NUMBER);

        driver.setCar(car);

        driverRequest = new DriverRequest(DRIVER_NAME, DRIVER_EMAIL, DRIVER_PHONE, Gender.MALE, CAR_ID);
        driverResponse = new DriverResponse(
                driver.getId(), driver.getName(), driver.getEmail(), AVERAGE_RATING,
                driver.getPhoneNumber(), driver.getGender(), driver.getDriverStatus(), CAR_ID
        );
    }

    @Test
    void shouldReturnDriverById() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverMapper.toDto(driver)).thenReturn(driverResponse);

        DriverResponse driverResponseTest = driverService.getDriverById(DRIVER_ID);

        assertNotNull(driverResponseTest);
        assertEquals(driver.getId(), driverResponseTest.id());
    }

    @Test
    void shouldAddDriverSuccessfully(){
        when(driverRepository.findDriverByPhoneNumberAndDeletedFalse(driverRequest.phoneNumber())).thenReturn(Optional.empty());
        when(driverRepository.findDriverByEmailAndDeletedFalse(driverRequest.email())).thenReturn(Optional.empty());
        when(carRepository.findCarByIdAndDeletedFalse(car.getId())).thenReturn(Optional.of(car));
        when(driverMapper.toEntity(driverRequest)).thenReturn(driver);

        driverService.addDriver(driverRequest);

        verify(driverRepository, times(1)).save(any(Driver.class));
        verify(kafkaProducer, times(1)).sendNewDriverBalance(any(NewDriverBalance.class));
    }

    @Test
    void shouldDeleteDriverSuccessfully() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID)).thenReturn(Optional.of(driver));

        driverService.deleteDriver(DRIVER_ID);

        assertTrue(driver.getDeleted());
        assertNull(driver.getCar());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    void shouldThrowExceptionWhenDriverNotFoundForDelete() {
        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> driverService.deleteDriver(DRIVER_ID));
    }

    @Test
    void shouldUpdateDriverSuccessfully() {
        DriverRequest updateRequest = new DriverRequest(NEW_DRIVER_NAME, NEW_DRIVER_EMAIL, NEW_DRIVER_PHONE, Gender.MALE, CAR_ID);

        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverRepository.findDriverByEmailAndDeletedFalse(updateRequest.email())).thenReturn(Optional.empty());
        when(driverRepository.findDriverByPhoneNumberAndDeletedFalse(updateRequest.phoneNumber())).thenReturn(Optional.empty());
        when(carRepository.findCarByIdAndDeletedFalse(updateRequest.carId())).thenReturn(Optional.of(car));

        driverService.updateDriver(DRIVER_ID, updateRequest);

        assertEquals(updateRequest.name(), driver.getName());
        assertEquals(updateRequest.email(), driver.getEmail());
        assertEquals(updateRequest.phoneNumber(), driver.getPhoneNumber());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        DriverRequest updateRequest = new DriverRequest(NEW_DRIVER_NAME, NEW_DRIVER_EMAIL, NEW_DRIVER_PHONE, Gender.MALE, CAR_ID);

        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverRepository.findDriverByEmailAndDeletedFalse(updateRequest.email())).thenReturn(Optional.of(new Driver()));

        assertThrows(ResourceAlreadyTakenException.class, () -> driverService.updateDriver(DRIVER_ID, updateRequest));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingPhoneNumber() {
        DriverRequest updateRequest = new DriverRequest(NEW_DRIVER_NAME, NEW_DRIVER_EMAIL, NEW_DRIVER_PHONE, Gender.MALE, CAR_ID);

        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverRepository.findDriverByPhoneNumberAndDeletedFalse(updateRequest.phoneNumber())).thenReturn(Optional.of(new Driver()));

        assertThrows(ResourceAlreadyTakenException.class, () -> driverService.updateDriver(DRIVER_ID, updateRequest));
    }

    @Test
    void shouldThrowExceptionWhenCarAlreadyTaken() {
        DriverRequest updateRequest = new DriverRequest(NEW_DRIVER_NAME, NEW_DRIVER_EMAIL, NEW_DRIVER_PHONE, Gender.MALE, CAR_ID);

        Car takenCar = new Car();
        takenCar.setId(2L);
        takenCar.setDriver(new Driver());

        when(driverRepository.findByIdAndDeletedFalse(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(carRepository.findCarByIdAndDeletedFalse(updateRequest.carId())).thenReturn(Optional.of(takenCar));

        assertThrows(ResourceAlreadyTakenException.class, () -> driverService.updateDriver(DRIVER_ID, updateRequest));
    }

    @Test
    void shouldThrowExceptionWhenDriverNotFoundById() {
        when(driverRepository.findByIdAndDeletedFalse(WRONG_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> driverService.getDriverById(WRONG_ID));
    }
}
