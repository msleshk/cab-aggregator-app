package cab.app.ratingservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    @Positive(message = "Ride id should be positive!")
    @NotNull(message = "Ride id should not be null!")
    private Long rideId;

    @Positive(message = "User id should be positive!")
    @NotNull(message = "User id should not be null!")
    private Long userId;

    @NotNull(message = "User role should not be null!")
    @Pattern(regexp = "^(PASSENGER|DRIVER)$", message = "User role must be either 'Passenger' or 'Driver'")
    private String userRole;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 255, message = "Comment cannot be longer than 255 characters")
    private String comment;
}
