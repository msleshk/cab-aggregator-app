package cab.app.ratingservice.service;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;

import java.math.BigDecimal;
import java.util.List;

public interface RatingService {
    void addRating(RatingRequest ratingRequest);

    void deleteRating(String id);

    void updateRating(String ratingId, RatingToUpdate rating);

    RatingResponse getRatingById(String id);

    List<RatingResponse> getRatingByUserIdAndRole(Long userId, String role);

    List<RatingResponse> getRatingByRideId(Long rideId);

    AverageRating getAverageRating(Long userId, String role);
}
