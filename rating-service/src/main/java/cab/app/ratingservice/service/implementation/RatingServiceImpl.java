package cab.app.ratingservice.service.implementation;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.exception.RatingAlreadyExistException;
import cab.app.ratingservice.exception.RatingNotFoundException;
import cab.app.ratingservice.exception.ValidationException;
import cab.app.ratingservice.model.Rating;
import cab.app.ratingservice.model.enums.Role;
import cab.app.ratingservice.repository.RatingRepository;
import cab.app.ratingservice.service.RatingService;
import cab.app.ratingservice.util.RatingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public RatingServiceImpl(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    @Override
    @Transactional
    public void addRating(RatingRequest ratingRequest) {
        // todo check if ride and user exist
        Role userRole = validateRole(ratingRequest.getUserRole());
        if (ratingRepository.findRatingByRideIdAndUserRole(ratingRequest.getRideId(), userRole).isPresent()){
            throw new RatingAlreadyExistException("Rating already was created!");
        }
        ratingRepository.save(ratingMapper.toEntity(ratingRequest));
    }

    @Override
    @Transactional
    public void deleteRating(String id) {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new RatingNotFoundException("Rating with this id not found!"));
        ratingRepository.delete(rating);
    }

    @Override
    @Transactional
    public void updateRating(String id, RatingToUpdate dto) {
        Rating ratingToUpdate = ratingRepository.findById(id).orElseThrow(() -> new RatingNotFoundException("Rating with this id not found!"));
        ratingToUpdate.setRating(dto.getRating());
        ratingToUpdate.setComment(dto.getComment());
        ratingRepository.save(ratingToUpdate);
    }

    @Override
    public RatingResponse getRatingById(String id) {
        return ratingRepository.findById(id).map(ratingMapper::toDto).orElseThrow(()->new RatingNotFoundException("Rating with this id not found!"));
    }

    @Override
    public List<RatingResponse> getRatingByUserIdAndRole(Long userId, String role) {
        Role userRole = validateRole(role);
        // todo check if at least 1 exist
        return ratingRepository.findByUserIdAndUserRole(userId, userRole)
                .stream().map(ratingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getRatingByRideId(Long rideId) {
        // todo check if at least 1 exist
        return ratingRepository.findRatingByRideId(rideId)
                .stream().map(ratingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AverageRating getAverageRating(Long userId, String role) {
        // todo check if user exist
        Role userRole = validateRole(role);
        return new AverageRating(userId, calculateRating(userId, userRole));
    }

    private Role validateRole(String role){
        Role userRole;
        try {
            userRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new ValidationException("No such role: " + role);
        }
        return userRole;
    }

    private Double calculateRating(Long userId, Role userRole){
        return ratingRepository.findByUserIdAndUserRole(userId, userRole)
                .stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0);
    }
}
