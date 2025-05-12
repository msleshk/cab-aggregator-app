package com.example.passengerservice.controller;

import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.dto.response.PassengerResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Passengers", description = "API for passengers")
@RequestMapping("/api/v1/passengers")
public interface PassengerApi {

    @Operation(summary = "Add a new passenger", description = "Creates a new passenger in the system")
    @PostMapping
    ResponseEntity<Void> addPassenger(@RequestBody @Valid PassengerRequest passengerRequest);

    @Operation(summary = "Delete a passenger", description = "Removes a passenger from the system by ID")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePassenger(@PathVariable("id") Long id);

    @Operation(summary = "Update passenger details", description = "Updates information of an existing passenger")
    @PatchMapping("/{id}")
    ResponseEntity<Void> updatePassenger(@PathVariable("id") Long id, @RequestBody @Valid PassengerRequest passengerRequest);

    @Operation(summary = "Get list of passengers", description = "Retrieves a paginated list of all passengers")
    @GetMapping
    ResponseEntity<PassengerResponseList> getPassengers(@RequestParam
                                                        @Min(value = 0, message = "Offset should be greater than 0!")
                                                        int offset,

                                                        @RequestParam
                                                        @Min(value = 1, message = "Limit should be greater than 1")
                                                        @Max(value = 100, message = "Limit should be less than 100")
                                                        int limit);

    @Operation(summary = "Get passenger by ID", description = "Retrieves details of a specific passenger by ID")
    @GetMapping("/{id}")
    ResponseEntity<PassengerResponse> getPassenger(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers);
}