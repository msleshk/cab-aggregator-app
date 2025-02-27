package cab.app.ratingservice.repository;

import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.model.Rating;
import cab.app.ratingservice.model.enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findRatingByRideId(Long rideId);
    Optional<Rating> findRatingByRideIdAndUserRole(Long rideId, Role role);
    List<Rating> findByUserIdAndUserRole(Long userId, Role role);
}
