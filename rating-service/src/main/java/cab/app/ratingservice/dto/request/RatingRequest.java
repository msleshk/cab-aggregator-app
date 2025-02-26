package cab.app.ratingservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    @Positive
    @NotNull
    private Long rideId;
    @Positive
    @NotNull
    private Long userId;
    @NotNull
    private String userRole;
    //todo regexp validation
    private Integer rating;
    private String comment;
}
