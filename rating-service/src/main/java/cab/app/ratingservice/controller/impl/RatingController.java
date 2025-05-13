package cab.app.ratingservice.controller.impl;

import cab.app.ratingservice.controller.RatingApi;
import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.dto.response.ResponseList;
import cab.app.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController implements RatingApi {

    private final RatingService ratingService;

    @Override
    public ResponseEntity<ResponseList<RatingResponse>> getAllRatings(int offset, int limit) {
        return ResponseEntity.ok().body(ratingService.getAllRating(offset, limit));
    }

    @Override
    public ResponseEntity<AverageRating> getAverageRatingByUser(Long userId, String role) {
        return ResponseEntity.ok().body(ratingService.getAverageRating(userId, role));
    }

    @Override
    public ResponseEntity<ResponseList<RatingResponse>> getRatingByRideId(Long rideId, int offset, int limit) {
        return ResponseEntity.ok().body(ratingService.getRatingByRideId(rideId, offset, limit));
    }

    @Override
    public ResponseEntity<ResponseList<RatingResponse>> getRatingByUserAndRole(Long userId, String role, int offset, int limit) {
        return ResponseEntity.ok().body(ratingService.getRatingByUserIdAndRole(userId, role, offset, limit));
    }

    @Override
    public ResponseEntity<RatingResponse> getRatingById(String id) {
        return ResponseEntity.ok().body(ratingService.getRatingById(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createRating(RatingRequest ratingRequest) {
        ratingService.addRating(ratingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRating(String id, RatingToUpdate update) {
        ratingService.updateRating(id, update);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRating(String id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }
}
