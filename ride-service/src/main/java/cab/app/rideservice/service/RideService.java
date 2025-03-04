package cab.app.rideservice.service;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.response.ResponseList;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.model.Ride;

import java.util.List;

public interface RideService {
    void createRide(RideRequest rideRequest);
    void deleteRide(Long rideId);
    void updateRide(Long rideId, RideRequest rideRequest);
    void updateRideStatus(Long rideId, String status);
    RideResponse getRide(Long rideId);
    ResponseList<RideResponse> getAllRides(int offset, int limit);
    ResponseList<RideResponse> getRidesByStatus(String status, int offset, int limit);
    ResponseList<RideResponse> getAllRidesByPassenger(Long passengerId, int offset, int limit);
    ResponseList<RideResponse> getAllRidesByDriver(Long driverId, int offset, int limit);
}
