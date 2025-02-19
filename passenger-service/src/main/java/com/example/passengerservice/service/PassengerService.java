package com.example.passengerservice.service;

import com.example.passengerservice.dto.PassengerRequest;
import com.example.passengerservice.dto.PassengerResponse;

import java.util.List;

public interface PassengerService {
    void addPassenger(PassengerRequest dto);

    void updatePassenger(Long id, PassengerRequest dto);

    void deletePassengerById(Long id);

    PassengerResponse getPassengerById(Long id);

    List<PassengerResponse> getAllPassengers();
}
