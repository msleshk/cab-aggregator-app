package cab.app.rideservice.service;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.request.RideToUpdate;
import cab.app.rideservice.dto.response.DriverResponse;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.exception.*;
import cab.app.rideservice.kafka.KafkaProducer;
import cab.app.rideservice.kafka.KafkaProducerConfig;
import cab.app.rideservice.kafka.KafkaTopic;
import cab.app.rideservice.model.Ride;
import cab.app.rideservice.model.enums.DriverStatus;
import cab.app.rideservice.model.enums.RideStatus;
import cab.app.rideservice.repository.RideRepository;
import cab.app.rideservice.service.implementation.CostCalculator;
import cab.app.rideservice.service.implementation.EntityValidator;
import cab.app.rideservice.service.implementation.RideServiceImpl;
import cab.app.rideservice.util.RideMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static cab.app.rideservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockBean({KafkaProducer.class, KafkaProducerConfig.class, KafkaTopic.class, KafkaTemplate.class})
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;
    @Mock
    private RideMapper rideMapper;
    @Mock
    private CostCalculator costCalculator;
    @Mock
    private EntityValidator validator;
    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private RideServiceImpl rideService;

    private Ride ride;
    private RideRequest rideRequest;
    private RideToUpdate rideToUpdate;
    private DriverResponse driverResponse;

    @BeforeEach
    void setUp() {
        ride = new Ride();
        ride.setId(RIDE_ID);
        ride.setPassengerId(USER_ID);
        ride.setDriverId(null);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setDeleted(false);

        rideRequest = new RideRequest(USER_ID, DEPARTURE_ADDRESS, ARRIVAL_ADDRESS, SHORT_TRIP_DISTANCE);
        rideToUpdate = new RideToUpdate(NEW_DEPARTURE_ADDRESS, NEW_ARRIVAL_ADDRESS, LONG_TRIP_DISTANCE);

        driverResponse = new DriverResponse(USER_ID, DriverStatus.AVAILABLE, RIDE_ID);
    }

    @Test
    void shouldCreateRideSuccessfully() {
        when(costCalculator.generateCost(SHORT_TRIP_DISTANCE)).thenReturn(BASE_FARE);
        when(rideMapper.toEntity(rideRequest)).thenReturn(ride);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        rideService.createRide(rideRequest);

        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void shouldDeleteRideSuccessfully() {
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.of(ride));

        rideService.deleteRide(RIDE_ID);

        assertTrue(ride.getDeleted());
        verify(rideRepository).save(ride);
    }

    @Test
    void shouldThrowExceptionIfRideNotFoundOnDelete() {
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.deleteRide(RIDE_ID));
    }

    @Test
    void shouldUpdateRideSuccessfully() {
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.of(ride));
        when(costCalculator.generateCost(LONG_TRIP_DISTANCE)).thenReturn(BASE_FARE);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        rideService.updateRide(RIDE_ID, rideToUpdate);

        assertEquals(rideToUpdate.departureAddress(), ride.getDepartureAddress());
        verify(rideRepository).save(ride);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingCompletedRide() {
        ride.setStatus(RideStatus.COMPLETED);
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.of(ride));

        assertThrows(InvalidStatusException.class, () -> rideService.updateRide(RIDE_ID, rideToUpdate));
    }

    @Test
    void shouldAssignDriverSuccessfully() {
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.of(ride));
        when(validator.getDriverById(USER_ID)).thenReturn(driverResponse);

        rideService.assignDriver(RIDE_ID, USER_ID);

        assertEquals(USER_ID, ride.getDriverId());
        assertEquals(RideStatus.ACCEPTED, ride.getStatus());
        verify(rideRepository).save(ride);
    }

    @Test
    void shouldThrowExceptionWhenAssigningDriverToAlreadyAssignedRide() {
        ride.setDriverId(USER_ID);
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.of(ride));

        assertThrows(BadRequestException.class, () -> rideService.assignDriver(RIDE_ID, USER_ID));
    }

    @Test
    void shouldGetRideSuccessfully() {
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.of(ride));
        when(rideMapper.toDto(ride)).thenReturn(new RideResponse(
                RIDE_ID,
                DRIVER_ID,
                USER_ID,
                DEPARTURE_ADDRESS,
                ARRIVAL_ADDRESS,
                STATUS_REQUESTED,
                LocalDateTime.now(),
                BASE_FARE
        ));
        RideResponse response = rideService.getRide(RIDE_ID);

        assertNotNull(response);
        assertEquals(RIDE_ID, response.id());
    }

    @Test
    void shouldGetAllRidesSuccessfully() {
        when(rideRepository.findAllByDeletedFalse(PageRequest.of(OFFSET, LIMIT)))
                .thenReturn(new PageImpl<>(List.of(ride)));
        when(rideMapper.toDto(ride)).thenReturn(new RideResponse(
                RIDE_ID,
                DRIVER_ID,
                USER_ID,
                DEPARTURE_ADDRESS,
                ARRIVAL_ADDRESS,
                STATUS_REQUESTED,
                LocalDateTime.now(),
                BASE_FARE
        ));

        var responseList = rideService.getAllRides(OFFSET, LIMIT);

        assertNotNull(responseList);
        assertEquals(1, responseList.responseList().size());
    }

    @Test
    void shouldThrowExceptionWhenRideNotFound() {
        when(rideRepository.findByIdAndDeletedFalse(RIDE_ID)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.getRide(RIDE_ID));
    }
}
