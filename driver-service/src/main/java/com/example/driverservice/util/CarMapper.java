package com.example.driverservice.util;

import com.example.driverservice.dto.car.CarRequest;
import com.example.driverservice.dto.car.CarResponse;
import com.example.driverservice.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(source = "driver.id", target = "driverId")
    CarResponse toDto(Car car);

    Car toEntity(CarRequest carRequest);
}
