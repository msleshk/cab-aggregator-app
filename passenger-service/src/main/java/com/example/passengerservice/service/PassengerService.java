package com.example.passengerservice.service;

import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.dto.response.PassengerResponseList;


public interface PassengerService {
    void addPassenger(PassengerRequest dto);

    void updatePassenger(Long id, PassengerRequest dto);

    void deletePassengerById(Long id);

    PassengerResponse getPassengerById(Long id);

    PassengerResponseList getAllPassengers(int offset, int limit);
}
