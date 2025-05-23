package cab.app.ratingservice.service.implementation;

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
import cab.app.ratingservice.service.RatingService;
import cab.app.ratingservice.util.RatingMapper;
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
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final EntityValidator validator;
    private final KafkaProducer kafkaProducer;


    @Override
    public ResponseList<RatingResponse> getAllRating(int offset, int limit) {
        return new ResponseList<>(ratingRepository.findAll(PageRequest.of(offset, limit))
                .getContent()
                .stream()
                .map(ratingMapper::toDto)
                .toList());
    }

    @Override
    @Transactional
    public void addRating(RatingRequest ratingRequest) {
        Role userRole = validateRole(ratingRequest.userRole());

        validator.checkIfUserExist(ratingRequest.userId(), userRole);

        RideResponse rideToRate = validator.getRideById(ratingRequest.rideId());

        if (!rideToRate.status().equals("COMPLETED")) {
            throw new RideNotCompletedException("Ride is not completed to rate!");
        }

        if (ratingRepository.findRatingByRideIdAndUserRole(ratingRequest.rideId(), userRole).isPresent()) {
            throw new RatingAlreadyExistException("Rating already was created!");
        }

        Rating rating = ratingMapper.toEntity(ratingRequest);

        validateRating(rideToRate, rating);
        switch (userRole) {
            case DRIVER -> {
                rating.setRatedUserRole(Role.PASSENGER);
                rating.setRatedUserId(rideToRate.passengerId());
            }
            case PASSENGER -> {
                rating.setRatedUserRole(Role.DRIVER);
                rating.setRatedUserId(rideToRate.driverId());
            }
        }

        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);
        sendUpdatedRating(rating);
    }

    private void sendUpdatedRating(Rating rating) {
        Role ratedUserRole = rating.getRatedUserRole();
        Long ratedUserId = rating.getRatedUserId();
        AverageRating averageRatingResponse = getAverageRating(ratedUserId, String.valueOf(ratedUserRole));
        switch (ratedUserRole) {
            case DRIVER -> kafkaProducer.sendDriverAvgTaring(averageRatingResponse);
            case PASSENGER -> kafkaProducer.sendPassengerAvgRating(averageRatingResponse);
        }
    }

    @Override
    @Transactional
    public void deleteRating(String id) {
        Rating rating = findRatingById(id);
        ratingRepository.delete(rating);
        sendUpdatedRating(rating);
    }

    @Override
    @Transactional
    public void updateRating(String id, RatingToUpdate dto) {
        Rating ratingToUpdate = findRatingById(id);
        ratingToUpdate.setRating(dto.rating());
        ratingToUpdate.setComment(dto.comment());
        ratingRepository.save(ratingToUpdate);
        sendUpdatedRating(ratingToUpdate);
    }

    @Override
    public RatingResponse getRatingById(String id) {
        return ratingMapper.toDto(findRatingById(id));
    }

    @Override
    public ResponseList<RatingResponse> getRatingByUserIdAndRole(Long userId, String role, int offset, int limit) {
        Role userRole = validateRole(role);
        return new ResponseList<>(ratingRepository.findByUserIdAndUserRole(userId, userRole, PageRequest.of(offset, limit))
                .getContent()
                .stream()
                .map(ratingMapper::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseList<RatingResponse> getRatingByRideId(Long rideId, int offset, int limit) {
        return new ResponseList<>(
                ratingRepository
                        .findRatingByRideId(rideId, PageRequest.of(offset, limit))
                        .getContent()
                        .stream()
                        .map(ratingMapper::toDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public AverageRating getAverageRating(Long userId, String role) {
        Role userRole = validateRole(role);
        validator.checkIfUserExist(userId, userRole);
        return new AverageRating(userId, calculateRating(userId, userRole));
    }

    private Rating findRatingById(String id) {
        return ratingRepository.findById(id).orElseThrow(() -> new RatingNotFoundException("Rating with this id not found!"));
    }

    private void validateRating(RideResponse ride, Rating rating) {
        switch (rating.getUserRole()) {
            case DRIVER -> {
                if (!Objects.equals(ride.driverId(), rating.getUserId())) {
                    throw new IllegalArgumentException("Wrong user id for this ride!");
                }
            }
            case PASSENGER -> {
                if (!Objects.equals(ride.passengerId(), rating.getUserId())) {
                    throw new IllegalArgumentException("Wrong user id for this ride!");
                }
            }
        }
    }

    private Role validateRole(String role) {
        Role userRole;
        try {
            userRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No such role: " + role);
        }
        return userRole;
    }

    private Double calculateRating(Long userId, Role userRole) {
        return ratingRepository.findRatingsByRatedUserIdAndUserRoleAfterDate(userId, userRole, LocalDateTime.now().minusYears(1))
                .stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(5);
    }
}
