package com.example.passengerservice.controller.impl;

import com.example.passengerservice.controller.PassengerApi;
import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.dto.response.PassengerResponseList;
import com.example.passengerservice.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PassengerController implements PassengerApi {

    private final PassengerService passengerService;

    @Override
    public ResponseEntity<Void> addPassenger(PassengerRequest passengerRequest) {
        passengerService.addPassenger(passengerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> deletePassenger(Long id) {
        passengerService.deletePassengerById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updatePassenger(Long id, PassengerRequest passengerRequest) {
        passengerService.updatePassenger(id, passengerRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PassengerResponseList> getPassengers(int offset, int limit) {
        return ResponseEntity.ok(passengerService.getAllPassengers(offset, limit));
    }

    @Override
    public ResponseEntity<PassengerResponse> getPassenger(Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }
}
