package cab.app.rideservice.controller;

import cab.app.rideservice.dto.request.RequestParams;
import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.response.ResponseList;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<Void> createRide(@RequestBody @Valid RideRequest rideRequest) {
        rideService.createRide(rideRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRide(@PathVariable("id") Long rideId, @RequestBody @Valid RideRequest rideRequest) {
        rideService.updateRide(rideId, rideRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<Void> updateRideStatus(@PathVariable("id") Long rideId, @PathVariable("status") String newRideStatus) {
        rideService.updateRideStatus(rideId, newRideStatus);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable("id") Long rideId) {
        rideService.deleteRide(rideId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable("id") Long rideId) {
        return ResponseEntity.ok().body(rideService.getRide(rideId));
    }

    @GetMapping
    public ResponseEntity<ResponseList<RideResponse>> getAllRides(@RequestBody @Valid RequestParams params) {
        return ResponseEntity.ok().body(rideService.getAllRides(params.offset(), params.limit()));
    }

    @GetMapping("/rides-by-passenger/{id}")
    public ResponseEntity<ResponseList<RideResponse>> getAllRidesByPassengerId(@PathVariable("id") Long passengerId, @RequestBody @Valid RequestParams params) {
        return ResponseEntity.ok().body(rideService.getAllRidesByPassenger(passengerId, params.offset(), params.limit()));
    }

    @GetMapping("/rides-by-driver/{id}")
    public ResponseEntity<ResponseList<RideResponse>> getAllRidesByDriverId(@PathVariable("id") Long driverId, @RequestBody @Valid RequestParams params) {
        return ResponseEntity.ok().body(rideService.getAllRidesByDriver(driverId, params.offset(), params.limit()));
    }

    @GetMapping("/rides-by-status/{status}")
    public ResponseEntity<ResponseList<RideResponse>> getRidesByStatus(@PathVariable("status") String status, @RequestBody @Valid RequestParams params) {
        return ResponseEntity.ok().body(rideService.getRidesByStatus(status, params.offset(), params.limit()));
    }
}
