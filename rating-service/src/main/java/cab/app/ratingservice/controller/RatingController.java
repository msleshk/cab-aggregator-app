package cab.app.ratingservice.controller;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.exception.ValidationException;
import cab.app.ratingservice.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/avg-rating/{id}/{role}")
    public ResponseEntity<AverageRating> getAverageRatingByUser(@PathVariable("id") Long userId, @PathVariable("role") String role) {
        return ResponseEntity.ok().body(ratingService.getAverageRating(userId, role));
    }

    @GetMapping("/ride/{id}")
    public ResponseEntity<List<RatingResponse>> getRatingByRideId(@PathVariable("id") Long rideId) {
        return ResponseEntity.ok().body(ratingService.getRatingByRideId(rideId));
    }

    @GetMapping("/user/{id}/{role}")
    public ResponseEntity<List<RatingResponse>> getRatingByUserAndRole(@PathVariable("id") Long userId, @PathVariable("role") String role) {
        return ResponseEntity.ok().body(ratingService.getRatingByUserIdAndRole(userId, role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getRatingByRatingId(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(ratingService.getRatingById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createRating(@RequestBody @Valid RatingRequest ratingRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        ratingService.addRating(ratingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRating(@PathVariable("id") String id, @RequestBody RatingToUpdate update, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        ratingService.updateRating(id, update);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable("id") String id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok().build();
    }
}
