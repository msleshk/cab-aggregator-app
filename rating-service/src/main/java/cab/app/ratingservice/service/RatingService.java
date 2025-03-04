package cab.app.ratingservice.service;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.dto.response.ResponseList;

import java.math.BigDecimal;
import java.util.List;

public interface RatingService {
    ResponseList<RatingResponse> getAllRating(int offset, int limit);

    void addRating(RatingRequest ratingRequest);

    void deleteRating(String id);

    void updateRating(String ratingId, RatingToUpdate rating);

    RatingResponse getRatingById(String id);

    ResponseList<RatingResponse> getRatingByUserIdAndRole(Long userId, String role, int offset, int limit);

    ResponseList<RatingResponse> getRatingByRideId(Long rideId, int offset, int limit);

    AverageRating getAverageRating(Long userId, String role);
}
