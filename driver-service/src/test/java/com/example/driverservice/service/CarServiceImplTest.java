package com.example.driverservice.service;

import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.dto.response.CarResponse;
import com.example.driverservice.exception.EntityNotFoundException;
import com.example.driverservice.exception.ResourceAlreadyTakenException;
import com.example.driverservice.model.Car;
import com.example.driverservice.repository.CarRepository;
import com.example.driverservice.service.implementation.CarServiceImpl;
import com.example.driverservice.util.CarMapper;
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
public class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;
    private CarRequest carRequest;
    private CarResponse carResponse;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setId(CAR_ID);
        car.setBrand(BRAND);
        car.setModel(MODEL);
        car.setColor(COLOR);
        car.setCarNumber(CAR_NUMBER);

        carRequest = new CarRequest(BRAND, MODEL, COLOR, CAR_NUMBER);
        carResponse = new CarResponse(CAR_ID, BRAND, MODEL, COLOR, CAR_NUMBER, null);
    }

    @Test
    void shouldReturnCarById() {
        when(carRepository.findCarByIdAndDeletedFalse(CAR_ID)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(carResponse);

        CarResponse carResponseTest = carService.getCarById(CAR_ID);

        assertNotNull(carResponseTest);
        assertEquals(car.getId(), carResponseTest.id());
    }

    @Test
    void shouldAddCarSuccessfully() {
        when(carRepository.findCarByCarNumberAndDeletedFalse(carRequest.carNumber())).thenReturn(Optional.empty());
        when(carMapper.toEntity(carRequest)).thenReturn(car);

        carService.addCar(carRequest);

        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateCar() {
        when(carRepository.findCarByCarNumberAndDeletedFalse(carRequest.carNumber())).thenReturn(Optional.of(car));

        assertThrows(ResourceAlreadyTakenException.class, () -> carService.addCar(carRequest));
    }

    @Test
    void shouldDeleteCarSuccessfully() {
        when(carRepository.findCarByIdAndDeletedFalse(CAR_ID)).thenReturn(Optional.of(car));

        carService.deleteCar(CAR_ID);

        assertTrue(car.getDeleted());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCar() {
        when(carRepository.findCarByIdAndDeletedFalse(CAR_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.deleteCar(CAR_ID));
    }

    @Test
    void shouldUpdateCarSuccessfully() {
        CarRequest updateRequest = new CarRequest(BRAND, MODEL, COLOR, NEW_CAR_NUMBER);

        when(carRepository.findCarByIdAndDeletedFalse(CAR_ID)).thenReturn(Optional.of(car));
        when(carRepository.findCarByCarNumberAndDeletedFalse(updateRequest.carNumber())).thenReturn(Optional.empty());

        carService.updateCar(CAR_ID, updateRequest);

        assertEquals(BRAND, car.getBrand());
        assertEquals(MODEL, car.getModel());
        assertEquals(COLOR, car.getColor());
        assertEquals(NEW_CAR_NUMBER, car.getCarNumber());

        verify(carRepository, times(1)).save(car);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingCarNumber() {
        CarRequest updateRequest = new CarRequest(BRAND, MODEL, COLOR, NEW_CAR_NUMBER);
        when(carRepository.findCarByIdAndDeletedFalse(CAR_ID)).thenReturn(Optional.of(car));
        when(carRepository.findCarByCarNumberAndDeletedFalse(updateRequest.carNumber())).thenReturn(Optional.of(new Car()));

        assertThrows(ResourceAlreadyTakenException.class, () -> carService.updateCar(CAR_ID, updateRequest));
    }

    @Test
    void shouldReturnCarByCarNumber() {
        when(carRepository.findCarByCarNumberAndDeletedFalse(CAR_NUMBER)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(carResponse);

        CarResponse response = carService.getCarByCarNumber(CAR_NUMBER);

        assertNotNull(response);
        assertEquals(CAR_NUMBER, response.carNumber());
    }

    @Test
    void shouldThrowExceptionWhenCarNotFoundByCarNumber() {
        when(carRepository.findCarByCarNumberAndDeletedFalse(NEW_CAR_NUMBER)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> carService.getCarByCarNumber(NEW_CAR_NUMBER));
    }
}
