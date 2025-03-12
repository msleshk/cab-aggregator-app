package cab.app.ratingservice.repository;

import cab.app.ratingservice.model.Rating;
import cab.app.ratingservice.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    @Query("{ 'ratedUserId': ?0, 'ratedUserRole': ?1, 'createdAt': { $gte: ?2 } }")
    List<Rating> findRatingsByRatedUserIdAndUserRoleAfterDate(Long ratedUserId, Role userRole, LocalDateTime date);
    Page<Rating> findRatingByRideId(Long rideId, Pageable pageable);
    Optional<Rating> findRatingByRideIdAndUserRole(Long rideId, Role role);
    Page<Rating> findByUserIdAndUserRole(Long userId, Role role, Pageable pageable);
}
