package cab.app.ratingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponse {
    private String id;
    private Long rideId;
    private Long userId;
    private String userRole;
    private Integer rating;
    private String comment;
}
