package cab.app.rideservice.service.implementation;

import cab.app.rideservice.dto.kafka.CreatePayment;
import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.request.RideToUpdate;
import cab.app.rideservice.dto.response.DriverResponse;
import cab.app.rideservice.dto.response.ResponseList;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.dto.kafka.UpdateDriverStatus;
import cab.app.rideservice.exception.BadRequestException;
import cab.app.rideservice.exception.DriverNotValidException;
import cab.app.rideservice.exception.InvalidStatusException;
import cab.app.rideservice.exception.RideNotFoundException;
import cab.app.rideservice.kafka.KafkaProducer;
import cab.app.rideservice.model.Ride;
import cab.app.rideservice.model.enums.DriverStatus;
import cab.app.rideservice.model.enums.RideStatus;
import cab.app.rideservice.repository.RideRepository;
import cab.app.rideservice.service.RideService;
import cab.app.rideservice.util.RideMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideStatusValidator statusValidator;
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final CostCalculator costCalculator;
    private final EntityValidator validator;
    private final KafkaProducer kafkaProducerService;

    @Override
    @Transactional
    public void createRide(RideRequest rideRequest) {
        validator.checkIfPassengerExist(rideRequest.passengerId());
        Ride ride = rideMapper.toEntity(rideRequest);

        ride.setStatus(RideStatus.REQUESTED);
        ride.setOrderDateTime(LocalDateTime.now());
        ride.setCost(costCalculator.generateCost(rideRequest.distance()));
        rideRepository.save(ride);
    }

    @Override
    @Transactional
    public void deleteRide(Long rideId) {
        Ride rideToDelete = findRideById(rideId);
        rideToDelete.setDeleted(true);
        rideRepository.save(rideToDelete);
    }

    @Override
    @Transactional
    public void updateRide(Long rideId, RideToUpdate rideRequest) {
        Ride rideToUpdate = findRideById(rideId);
        if (rideToUpdate.getStatus() == RideStatus.COMPLETED || rideToUpdate.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidStatusException("You can't update ride with completed or cancelled status");
        }
        Ride updatedRide = updateRideFromDto(rideToUpdate, rideRequest);

        rideRepository.save(updatedRide);
    }

    @Override
    @Transactional
    public void assignDriver(Long rideId, Long driverId) {
        Ride ride = findRideById(rideId);
        if (ride.getDriverId() != null) {
            throw new BadRequestException("Driver for this ride already exist!");
        }
        DriverResponse driver = validator.getDriverById(driverId);
        validateDriver(driver);
        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ACCEPTED);
        rideRepository.save(ride);
        sendDriverStatusUpdate(driverId, DriverStatus.UNAVAILABLE);
    }

    private void validateDriver(DriverResponse driver) {
        if (Objects.equals(driver.status(), DriverStatus.UNAVAILABLE)) {
            throw new DriverNotValidException("Driver is unavailable!");
        }
        if (driver.carId() == null) {
            throw new DriverNotValidException("Driver doesn't have a car!");
        }
    }

    private void sendPaymentUpdate(Long rideId) {
        Ride ride = findRideById(rideId);
        CreatePayment payment = CreatePayment.builder()
                .rideId(rideId)
                .passengerId(ride.getPassengerId())
                .driverId(ride.getDriverId())
                .cost(ride.getCost())
                .build();
        kafkaProducerService.sendNewPayment(payment);
    }

    private void sendDriverStatusUpdate(Long driverId, DriverStatus newStatus) {
        kafkaProducerService.sendDriverStatusUpdate(UpdateDriverStatus.builder()
                .driverId(driverId)
                .status(newStatus)
                .build()
        );
    }

    @Override
    @Transactional
    public void updateRideStatus(Long rideId, String status) {
        Ride rideToUpdate = findRideById(rideId);
        RideStatus newStatus = statusValidator.validateRideStatus(rideToUpdate.getStatus(), RideStatus.valueOf(status.toUpperCase()));
        rideToUpdate.setStatus(RideStatus.valueOf(newStatus.toString()));
        if (newStatus.equals(RideStatus.COMPLETED)) {
            sendPaymentUpdate(rideId);
            sendDriverStatusUpdate(rideToUpdate.getDriverId(), DriverStatus.AVAILABLE);
        }
    }

    @Override
    public RideResponse getRide(Long rideId) {
        return rideMapper.toDto(findRideById(rideId));
    }

    @Override
    public ResponseList<RideResponse> getAllRides(int offset, int limit) {
        return new ResponseList<>(
                rideRepository
                        .findAllByDeletedFalse(PageRequest.of(offset, limit))
                        .getContent()
                        .stream()
                        .map(rideMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseList<RideResponse> getRidesByStatus(String status, int offset, int limit) {
        return new ResponseList<>(
                rideRepository
                        .findAllByDeletedFalseAndStatus(RideStatus.valueOf(status.toUpperCase()), PageRequest.of(offset, limit))
                        .getContent()
                        .stream()
                        .map(rideMapper::toDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public ResponseList<RideResponse> getAllRidesByPassenger(Long passengerId, int offset, int limit) {
        return new ResponseList<>(
                rideRepository
                        .findAllByDeletedFalseAndPassengerId(passengerId, PageRequest.of(offset, limit))
                        .getContent()
                        .stream()
                        .map(rideMapper::toDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public ResponseList<RideResponse> getAllRidesByDriver(Long driverId, int offset, int limit) {
        return new ResponseList<>(
                rideRepository
                        .findAllByDeletedFalseAndDriverId(driverId, PageRequest.of(offset, limit))
                        .getContent()
                        .stream()
                        .map(rideMapper::toDto)
                        .collect(Collectors.toList()));
    }

    private Ride findRideById(Long rideId) {
        return rideRepository.findByIdAndDeletedFalse(rideId).orElseThrow(() -> new RideNotFoundException("Ride with this id not found!"));
    }

    private Ride updateRideFromDto(Ride rideToUpdate, RideToUpdate rideRequest) {
        rideToUpdate.setDepartureAddress(rideRequest.departureAddress());
        rideToUpdate.setArrivalAddress(rideRequest.arrivalAddress());
        rideToUpdate.setUpdatedAt(LocalDateTime.now());
        rideToUpdate.setDistance(rideRequest.distance());
        rideToUpdate.setCost(costCalculator.generateCost(rideRequest.distance()));

        return rideToUpdate;
    }
}
