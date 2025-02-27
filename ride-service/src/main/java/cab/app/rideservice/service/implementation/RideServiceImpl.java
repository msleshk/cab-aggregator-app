package cab.app.rideservice.service.implementation;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.exception.InvalidStatusException;
import cab.app.rideservice.exception.RideNotFoundException;
import cab.app.rideservice.model.Ride;
import cab.app.rideservice.model.enums.RideStatus;
import cab.app.rideservice.repository.RideRepository;
import cab.app.rideservice.service.RideService;
import cab.app.rideservice.util.RideMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RideServiceImpl implements RideService {
    private final RideStatusValidator statusValidator;
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final CostCalculator costCalculator;

    public RideServiceImpl(RideStatusValidator statusValidator, RideRepository rideRepository, RideMapper rideMapper, CostCalculator costCalculator) {
        this.statusValidator = statusValidator;
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
        this.costCalculator = costCalculator;
    }

    @Override
    @Transactional
    public void createRide(RideRequest rideRequest) {
        // todo check if driver and passenger exist
        Ride ride = rideMapper.toEntity(rideRequest);
        if (ride.getDriverId() != null)
            ride.setStatus(RideStatus.ACCEPTED);
        else
            ride.setStatus(RideStatus.REQUESTED);

        ride.setOrderDateTime(LocalDateTime.now());
        ride.setCost(costCalculator.generateCost(rideRequest.getDistance()));
        rideRepository.save(ride);
    }

    @Override
    @Transactional
    public void deleteRide(Long rideId) {
        Ride rideToDelete = rideRepository.findByIdAndDeletedFalse(rideId).orElseThrow(()->new RideNotFoundException("Ride with this id not found!"));
        rideToDelete.setDeleted(true);
        rideRepository.save(rideToDelete);
    }

    @Override
    @Transactional
    public void updateRide(Long rideId, RideRequest rideRequest) {
        Ride rideToUpdate = rideRepository.findByIdAndDeletedFalse(rideId).orElseThrow(() -> new RideNotFoundException("Ride with this id not found!"));
        // todo check driver and passenger existence
        if (rideToUpdate.getStatus() == RideStatus.COMPLETED || rideToUpdate.getStatus() == RideStatus.CANCELLED){
            throw new InvalidStatusException("You can't update ride with completed or cancelled status");
        }
        if (rideRequest.getDriverId() != null)
            rideToUpdate.setStatus(RideStatus.ACCEPTED);
        else
            rideToUpdate.setStatus(RideStatus.REQUESTED);

        Ride updatedRide = updateRideFromDto(rideToUpdate, rideRequest);

        rideRepository.save(updatedRide);
    }

    @Override
    @Transactional
    public void updateRideStatus(Long rideId, String status) {
        Ride rideToUpdate = rideRepository.findByIdAndDeletedFalse(rideId).orElseThrow(()->new RideNotFoundException("Ride with this id not found!"));
        statusValidator.validateRideStatus(rideToUpdate.getStatus(), RideStatus.valueOf(status.toUpperCase()));
        rideToUpdate.setStatus(RideStatus.valueOf(status.toUpperCase()));
    }

    @Override
    public RideResponse getRide(Long rideId) {
        return rideRepository.findByIdAndDeletedFalse(rideId).map(rideMapper::toDto).orElseThrow(()-> new RideNotFoundException("Ride with this id not found!"));
    }

    @Override
    public List<RideResponse> getAllRides() {
        return rideRepository.findAllByDeletedFalse()
                .stream().map(rideMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RideResponse> getRidesByStatus(String status) {
        return rideRepository.findAllByDeletedFalseAndStatus(RideStatus.valueOf(status.toUpperCase()))
                .stream().map(rideMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RideResponse> getAllRidesByPassenger(Long passengerId) {
        return rideRepository.findAllByDeletedFalseAndPassengerId(passengerId)
                .stream().map(rideMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RideResponse> getAllRidesByDriver(Long driverId) {
        return rideRepository.findAllByDeletedFalseAndDriverId(driverId)
                .stream().map(rideMapper::toDto)
                .collect(Collectors.toList());
    }

    private Ride updateRideFromDto(Ride rideToUpdate, RideRequest rideRequest){
        rideToUpdate.setDriverId(rideRequest.getPassengerId());
        rideToUpdate.setPassengerId(rideRequest.getPassengerId());
        rideToUpdate.setDepartureAddress(rideRequest.getDepartureAddress());
        rideToUpdate.setArrivalAddress(rideRequest.getArrivalAddress());
        rideToUpdate.setUpdatedAt(LocalDateTime.now());
        rideToUpdate.setDistance(rideRequest.getDistance());
        rideToUpdate.setCost(costCalculator.generateCost(rideRequest.getDistance()));

        return rideToUpdate;
    }
}
