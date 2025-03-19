package cab.app.ratingservice.controller;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.dto.response.ResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ratings", description = "API for managing ratings")
@RequestMapping("api/v1/rating")
public interface RatingApi {

    @Operation(summary = "Get all ratings", description = "Retrieves a paginated list of all ratings")
    @GetMapping
    ResponseEntity<ResponseList<RatingResponse>> getAllRatings(
            @RequestParam @Min(value = 0, message = "Offset should be greater than 0!") int offset,
            @RequestParam @Min(value = 1, message = "Limit should be greater than 1")
            @Max(value = 100, message = "Limit should be less than 100") int limit
    );

    @Operation(summary = "Get average rating", description = "Retrieves the average rating of a user by ID and role")
    @GetMapping("/avg-rating/{id}/{role}")
    ResponseEntity<AverageRating> getAverageRatingByUser(@PathVariable("id") Long userId, @PathVariable("role") String role);

    @Operation(summary = "Get ratings by ride ID", description = "Retrieves ratings associated with a specific ride")
    @GetMapping("/ride/{id}")
    ResponseEntity<ResponseList<RatingResponse>> getRatingByRideId(
            @PathVariable("id") Long rideId,
            @RequestParam @Min(value = 0, message = "Offset should be greater than 0!") int offset,
            @RequestParam @Min(value = 1, message = "Limit should be greater than 1")
            @Max(value = 100, message = "Limit should be less than 100") int limit
    );

    @Operation(summary = "Get ratings by user and role", description = "Retrieves ratings of a user based on their role")
    @GetMapping("/user/{id}/{role}")
    ResponseEntity<ResponseList<RatingResponse>> getRatingByUserAndRole(
            @PathVariable("id") Long userId,
            @PathVariable("role") String role,
            @RequestParam @Min(value = 0, message = "Offset should be greater than 0!") int offset,
            @RequestParam @Min(value = 1, message = "Limit should be greater than 1")
            @Max(value = 100, message = "Limit should be less than 100") int limit
    );

    @Operation(summary = "Get rating by ID", description = "Retrieves a rating by its unique ID")
    @GetMapping("/{id}")
    ResponseEntity<RatingResponse> getRatingById(@PathVariable("id") String id);

    @Operation(summary = "Create a new rating", description = "Adds a new rating to the system")
    @PostMapping
    ResponseEntity<Void> createRating(@RequestBody @Valid RatingRequest ratingRequest);

    @Operation(summary = "Update rating", description = "Modifies an existing rating by ID")
    @PatchMapping("/{id}")
    ResponseEntity<Void> updateRating(@PathVariable("id") String id, @RequestBody @Valid RatingToUpdate update);

    @Operation(summary = "Delete rating", description = "Removes a rating from the system by ID")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteRating(@PathVariable("id") String id);
}
