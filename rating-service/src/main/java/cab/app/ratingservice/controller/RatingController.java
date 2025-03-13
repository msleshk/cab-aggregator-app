package cab.app.ratingservice.controller;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.request.RequestParams;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.dto.response.ResponseList;
import cab.app.ratingservice.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<ResponseList<RatingResponse>> getAllRating(@RequestBody @Valid RequestParams params){
        return ResponseEntity.ok().body(ratingService.getAllRating(params.offset(), params.limit()));
    }

    @GetMapping("/avg-rating/{id}/{role}")
    public ResponseEntity<AverageRating> getAverageRatingByUser(@PathVariable("id") Long userId, @PathVariable("role") String role) {
        return ResponseEntity.ok().body(ratingService.getAverageRating(userId, role));
    }

    @GetMapping("/ride/{id}")
    public ResponseEntity<ResponseList<RatingResponse>> getRatingByRideId(@PathVariable("id") Long rideId, @RequestBody @Valid RequestParams params) {
        return ResponseEntity.ok().body(ratingService.getRatingByRideId(rideId, params.offset(), params.limit()));
    }

    @GetMapping("/user/{id}/{role}")
    public ResponseEntity<ResponseList<RatingResponse>> getRatingByUserAndRole(@PathVariable("id") Long userId, @PathVariable("role") String role, @RequestBody @Valid RequestParams params) {
        return ResponseEntity.ok().body(ratingService.getRatingByUserIdAndRole(userId, role, params.offset(), params.limit()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getRatingByRatingId(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(ratingService.getRatingById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createRating(@RequestBody @Valid RatingRequest ratingRequest) {
        ratingService.addRating(ratingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRating(@PathVariable("id") String id, @RequestBody RatingToUpdate update) {
        ratingService.updateRating(id, update);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable("id") String id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }
}
