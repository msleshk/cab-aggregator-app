package com.example.passengerservice.util;

import com.example.passengerservice.dto.PassengerRequest;
import com.example.passengerservice.dto.PassengerResponse;
import com.example.passengerservice.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    @Mapping(source = "phone", target = "phoneNumber")
    PassengerResponse toDto(Passenger passenger);

    @Mapping(source = "phoneNumber", target = "phone")
    Passenger toEntity(PassengerRequest dto);
}
