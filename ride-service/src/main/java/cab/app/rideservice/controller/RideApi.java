package cab.app.rideservice.controller;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.request.RideToUpdate;
import cab.app.rideservice.dto.response.ResponseList;
import cab.app.rideservice.dto.response.RideResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rides", description = "API for managing rides")
@RequestMapping("/api/v1/rides")
public interface RideApi {

    @Operation(summary = "Create a new ride", description = "Creates a new ride in the system")
    @PostMapping
    ResponseEntity<Void> createRide(@RequestBody @Valid RideRequest rideRequest);

    @Operation(summary = "Update ride details", description = "Updates an existing ride by ID")
    @PatchMapping("/{id}")
    ResponseEntity<Void> updateRide(@PathVariable("id") @Positive(message = "Ride id must be greater than 0!") Long rideId,
                                    @RequestBody @Valid RideToUpdate rideToUpdateRequest);

    @Operation(summary = "Assign a driver to a ride", description = "Assigns a driver to a specific ride")
    @PatchMapping("/{id}/set-driver/{driverId}")
    ResponseEntity<Void> setDriver(@PathVariable("id") @Positive(message = "Ride id must be greater than 0!") Long rideId,
                                   @PathVariable("driverId") @Positive(message = "Driver id must be greater than 0!") Long driverId);

    @Operation(summary = "Update ride status", description = "Updates the status of a ride")
    @PatchMapping("/{id}/{status}")
    ResponseEntity<Void> updateRideStatus(@PathVariable("id") @Positive(message = "Ride id must be greater than 0!") Long rideId,
                                          @PathVariable("status") String newRideStatus);

    @Operation(summary = "Delete a ride", description = "Removes a ride from the system by ID")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteRide(@PathVariable("id") @Positive(message = "Ride id must be greater than 0!") Long rideId);

    @Operation(summary = "Get ride by ID", description = "Retrieves details of a ride by ID")
    @GetMapping("/{id}")
    ResponseEntity<RideResponse> getRideById(@PathVariable("id") @Positive(message = "Ride id must be greater than 0!") Long rideId);

    @Operation(summary = "Get all rides", description = "Retrieves a paginated list of all rides")
    @GetMapping
    ResponseEntity<ResponseList<RideResponse>> getAllRides(
            @RequestParam @Min(value = 0, message = "Offset must be greater than or equal to 0!") int offset,
            @RequestParam @Min(value = 1, message = "Limit must be greater than or equal to 1!")
            @Max(value = 100, message = "Limit must be less than or equal to 100!") int limit);

    @Operation(summary = "Get rides by passenger ID", description = "Retrieves a paginated list of rides for a given passenger")
    @GetMapping("/rides-by-passenger/{id}")
    ResponseEntity<ResponseList<RideResponse>> getAllRidesByPassengerId(
            @PathVariable("id") @Positive(message = "Passenger id must be greater than 0!") Long passengerId,
            @RequestParam @Min(value = 0, message = "Offset must be greater than or equal to 0!") int offset,
            @RequestParam @Min(value = 1, message = "Limit must be greater than or equal to 1!")
            @Max(value = 100, message = "Limit must be less than or equal to 100!") int limit);

    @Operation(summary = "Get rides by driver ID", description = "Retrieves a paginated list of rides for a given driver")
    @GetMapping("/rides-by-driver/{id}")
    ResponseEntity<ResponseList<RideResponse>> getAllRidesByDriverId(
            @PathVariable("id") @Positive(message = "Driver id must be greater than 0!") Long driverId,
            @RequestParam @Min(value = 0, message = "Offset must be greater than or equal to 0!") int offset,
            @RequestParam @Min(value = 1, message = "Limit must be greater than or equal to 1!")
            @Max(value = 100, message = "Limit must be less than or equal to 100!") int limit);

    @Operation(summary = "Get rides by status", description = "Retrieves a paginated list of rides with a specific status")
    @GetMapping("/rides-by-status/{status}")
    ResponseEntity<ResponseList<RideResponse>> getRidesByStatus(
            @PathVariable("status") String status,
            @RequestParam @Min(value = 0, message = "Offset must be greater than or equal to 0!") int offset,
            @RequestParam @Min(value = 1, message = "Limit must be greater than or equal to 1!")
            @Max(value = 100, message = "Limit must be less than or equal to 100!") int limit);
}
