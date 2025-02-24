package cab.app.rideservice.service;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.model.Ride;

import java.util.List;

public interface RideService {
    public void createRide(RideRequest rideRequest);
    public void deleteRide(Long rideId);
    public void updateRide(Long rideId, RideRequest rideRequest);
    public void updateRideStatus(Long rideId, String status);
    public RideResponse getRide(Long rideId);
    public List<RideResponse> getAllRides();
    public List<RideResponse> getRidesByStatus(String status);
    public List<RideResponse> getAllRidesByPassenger(Long passengerId);
    public List<RideResponse> getAllRidesByDriver(Long driverId);
}
