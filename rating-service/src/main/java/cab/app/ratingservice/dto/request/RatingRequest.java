package cab.app.ratingservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

public record RatingRequest (
        @Positive(message = "Ride id should be positive!")
        @NotNull(message = "Ride id should not be null!")
        Long rideId,

        @Positive(message = "User id should be positive!")
        @NotNull(message = "User id should not be null!")
        Long userId,

        @NotNull(message = "User role should not be null!")
        @Pattern(regexp = "^(PASSENGER|DRIVER)$", message = "User role must be either 'Passenger' or 'Driver'")
        String userRole,

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        Integer rating,

        @Size(max = 255, message = "Comment cannot be longer than 255 characters")
        String comment
) {
}
