package cab.app.ratingservice.dto.response.ride;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {
    private Long id;
    private Long driverId;
    private Long passengerId;
    private String status;
}
