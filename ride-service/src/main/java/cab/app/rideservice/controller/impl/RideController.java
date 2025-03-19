package cab.app.rideservice.controller.impl;

import cab.app.rideservice.controller.RideApi;
import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.request.RideToUpdate;
import cab.app.rideservice.dto.response.ResponseList;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RideController implements RideApi {

    private final RideService rideService;

    @Override
    public ResponseEntity<Void> createRide(RideRequest rideRequest) {
        rideService.createRide(rideRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> updateRide(Long rideId, RideToUpdate rideToUpdateRequest) {
        rideService.updateRide(rideId, rideToUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setDriver(Long rideId, Long driverId) {
        rideService.assignDriver(rideId, driverId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateRideStatus(Long rideId, String newRideStatus) {
        rideService.updateRideStatus(rideId, newRideStatus);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteRide(Long rideId) {
        rideService.deleteRide(rideId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<RideResponse> getRideById(Long rideId) {
        return ResponseEntity.ok().body(rideService.getRide(rideId));
    }

    @Override
    public ResponseEntity<ResponseList<RideResponse>> getAllRides(int offset, int limit) {
        return ResponseEntity.ok().body(rideService.getAllRides(offset, limit));
    }

    @Override
    public ResponseEntity<ResponseList<RideResponse>> getAllRidesByPassengerId(Long passengerId, int offset, int limit) {
        return ResponseEntity.ok().body(rideService.getAllRidesByPassenger(passengerId, offset, limit));
    }

    @Override
    public ResponseEntity<ResponseList<RideResponse>> getAllRidesByDriverId(Long driverId, int offset, int limit) {
        return ResponseEntity.ok().body(rideService.getAllRidesByDriver(driverId, offset, limit));
    }

    @Override
    public ResponseEntity<ResponseList<RideResponse>> getRidesByStatus(String status, int offset, int limit) {
        return ResponseEntity.ok().body(rideService.getRidesByStatus(status, offset, limit));
    }
}

