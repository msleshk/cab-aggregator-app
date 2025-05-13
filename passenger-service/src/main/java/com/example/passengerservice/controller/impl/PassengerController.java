package com.example.passengerservice.controller.impl;

import com.example.passengerservice.controller.PassengerApi;
import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.dto.response.PassengerResponseList;
import com.example.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PassengerController implements PassengerApi {

    private final PassengerService passengerService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    public ResponseEntity<Void> addPassenger(PassengerRequest passengerRequest) {
        log.info("Adding passenger with name={}", passengerRequest.name());
        passengerService.addPassenger(passengerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    public ResponseEntity<Void> deletePassenger(Long id) {
        log.info("Deleting passenger with id={}", id);
        passengerService.deletePassengerById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    public ResponseEntity<Void> updatePassenger(Long id, PassengerRequest passengerRequest) {
        log.info("Updating passenger with id={}", id);
        passengerService.updatePassenger(id, passengerRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<PassengerResponseList> getPassengers(int offset, int limit) {
        log.info("Fetching passengers");
        return ResponseEntity.ok(passengerService.getAllPassengers(offset, limit));
    }

    @Override
    public ResponseEntity<PassengerResponse> getPassenger(Long id, @RequestHeader HttpHeaders headers) {
        log.info("Fetching passenger with id={}", id);
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }
}
