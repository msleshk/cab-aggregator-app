package cab.app.ratingservice.service;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.dto.response.ResponseList;
import cab.app.ratingservice.dto.response.ride.RideResponse;
import cab.app.ratingservice.exception.RatingAlreadyExistException;
import cab.app.ratingservice.exception.RatingNotFoundException;
import cab.app.ratingservice.exception.RideNotCompletedException;
import cab.app.ratingservice.kafka.KafkaProducer;
import cab.app.ratingservice.model.Rating;
import cab.app.ratingservice.model.enums.Role;
import cab.app.ratingservice.repository.RatingRepository;
import cab.app.ratingservice.service.implementation.EntityValidator;
import cab.app.ratingservice.service.implementation.RatingServiceImpl;
import cab.app.ratingservice.util.RatingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import static cab.app.ratingservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private EntityValidator validator;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating rating;
    private RatingRequest ratingRequest;
    private RatingResponse ratingResponse;
    private RideResponse rideResponse;
    private RatingToUpdate ratingToUpdate;

    @BeforeEach
    void setUp() {
        rating = new Rating();
        rating.setId(RATING_ID);
        rating.setUserId(USER_ID);
        rating.setRatedUserId(RATED_USER_ID);
        rating.setUserRole(Role.valueOf(DRIVER_ROLE));
        rating.setRatedUserRole(Role.valueOf(PASSENGER_ROLE));
        rating.setRating(RATING_VALUE);
        rating.setComment(COMMENT);
        rating.setCreatedAt(TIMESTAMP);

        ratingRequest = mock(RatingRequest.class);
        ratingResponse = mock(RatingResponse.class);
        rideResponse = mock(RideResponse.class);
        ratingToUpdate = mock(RatingToUpdate.class);
    }

    @Test
    void shouldGetAllRatings() {
        Page<Rating> page = new PageImpl<>(Collections.singletonList(rating));
        when(ratingRepository.findAll(PageRequest.of(OFFSET, LIMIT))).thenReturn(page);
        when(ratingMapper.toDto(rating)).thenReturn(ratingResponse);

        ResponseList<RatingResponse> response = ratingService.getAllRating(OFFSET, LIMIT);

        assertNotNull(response);
        assertFalse(response.responseList().isEmpty());
    }

    @Test
    void shouldAddRatingSuccessfully() {
        when(ratingRequest.userRole()).thenReturn(DRIVER_ROLE);
        when(validator.getRideById(RIDE_ID)).thenReturn(rideResponse);
        when(rideResponse.status()).thenReturn(RIDE_STATUS_COMPLETED);
        when(ratingRepository.findRatingByRideIdAndUserRole(RIDE_ID, Role.valueOf(DRIVER_ROLE)))
                .thenReturn(Optional.empty());
        when(ratingMapper.toEntity(ratingRequest)).thenReturn(rating);
        when(ratingRequest.rideId()).thenReturn(RIDE_ID);
        when(ratingRequest.userId()).thenReturn(USER_ID);
        when(rideResponse.driverId()).thenReturn(USER_ID);
        when(rideResponse.passengerId()).thenReturn(USER_ID);

        ratingService.addRating(ratingRequest);

        verify(ratingRepository, times(1)).save(rating);
        verify(kafkaProducer, times(1)).sendPassengerAvgRating(any(AverageRating.class));
    }

    @Test
    void shouldThrowExceptionWhenRideNotCompleted() {
        when(ratingRequest.userRole()).thenReturn(DRIVER_ROLE);
        when(ratingRequest.rideId()).thenReturn(RIDE_ID);
        when(validator.getRideById(RIDE_ID)).thenReturn(rideResponse);
            when(rideResponse.status()).thenReturn(RIDE_STATUS_IN_PROGRESS);

        assertThrows(RideNotCompletedException.class, () -> ratingService.addRating(ratingRequest));
    }

    @Test
    void shouldThrowExceptionIfRatingAlreadyExists() {
        when(ratingRequest.userRole()).thenReturn(DRIVER_ROLE);
        when(ratingRequest.rideId()).thenReturn(RIDE_ID);
        when(validator.getRideById(RIDE_ID)).thenReturn(rideResponse);
        when(rideResponse.status()).thenReturn(RIDE_STATUS_COMPLETED);
        when(ratingRepository.findRatingByRideIdAndUserRole(RIDE_ID, Role.valueOf(DRIVER_ROLE)))
                .thenReturn(Optional.of(rating));

        assertThrows(RatingAlreadyExistException.class, () -> ratingService.addRating(ratingRequest));
    }

    @Test
    void shouldDeleteRating() {
        when(ratingRepository.findById(RATING_ID)).thenReturn(Optional.of(rating));

        ratingService.deleteRating(RATING_ID);

        verify(ratingRepository, times(1)).delete(rating);
        verify(kafkaProducer, times(1)).sendPassengerAvgRating(any(AverageRating.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentRating() {
        when(ratingRepository.findById(RATING_ID)).thenReturn(Optional.empty());

        assertThrows(RatingNotFoundException.class, () -> ratingService.deleteRating(RATING_ID));
    }

    @Test
    void shouldUpdateRating() {
        when(ratingToUpdate.rating()).thenReturn(UPDATED_RATING_VALUE);
        when(ratingToUpdate.comment()).thenReturn(UPDATED_COMMENT);
        when(ratingRepository.findById(RATING_ID)).thenReturn(Optional.of(rating));

        ratingService.updateRating(RATING_ID, ratingToUpdate);

        assertEquals(UPDATED_RATING_VALUE, rating.getRating());
        assertEquals(UPDATED_COMMENT, rating.getComment());
        verify(ratingRepository, times(1)).save(rating);
        verify(kafkaProducer, times(1)).sendPassengerAvgRating(any(AverageRating.class));
    }

    @Test
    void shouldGetRatingById() {
        when(ratingRepository.findById(RATING_ID)).thenReturn(Optional.of(rating));
        when(ratingMapper.toDto(rating)).thenReturn(ratingResponse);

        RatingResponse response = ratingService.getRatingById(RATING_ID);

        assertNotNull(response);
    }

    @Test
    void shouldThrowExceptionWhenRatingNotFoundById() {
        when(ratingRepository.findById(RATING_ID)).thenReturn(Optional.empty());

        assertThrows(RatingNotFoundException.class, () -> ratingService.getRatingById(RATING_ID));
    }

    @Test
    void shouldGetAverageRating() {
        doNothing().when(validator).checkIfUserExist(USER_ID, Role.valueOf(DRIVER_ROLE));

        doReturn(Collections.singletonList(rating)).when(ratingRepository)
                .findRatingsByRatedUserIdAndUserRoleAfterDate(
                        eq(USER_ID),
                        eq(Role.valueOf(DRIVER_ROLE)),
                        any(LocalDateTime.class)
                );

        AverageRating averageRating = ratingService.getAverageRating(USER_ID, DRIVER_ROLE);

        assertNotNull(averageRating);
        assertEquals(USER_ID, averageRating.id());
    }

}

