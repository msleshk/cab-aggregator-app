package com.example.driverservice.util;

import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    @Mapping(source = "car.id", target = "carId")
    DriverResponse toDto(Driver driver);

    @Mapping(target = "car", ignore = true)
    Driver toEntity(DriverRequest driverRequest);
}
