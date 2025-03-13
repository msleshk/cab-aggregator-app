package com.example.passengerservice.controller;

import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.request.PassengerRequestParams;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.dto.response.PassengerResponseList;
import com.example.passengerservice.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @PostMapping
    public ResponseEntity<Void> addPassenger(@RequestBody @Valid PassengerRequest passengerRequest) {
        passengerService.addPassenger(passengerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable("id") Long id) {
        passengerService.deletePassengerById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassenger(@PathVariable("id") Long id, @RequestBody @Valid PassengerRequest passengerRequest) {
        passengerService.updatePassenger(id, passengerRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PassengerResponseList> getPassengers(@RequestBody @Valid PassengerRequestParams requestParams) {
        return ResponseEntity.ok(passengerService.getAllPassengers(requestParams.offset(), requestParams.limit()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassenger(@PathVariable("id") Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }
}
