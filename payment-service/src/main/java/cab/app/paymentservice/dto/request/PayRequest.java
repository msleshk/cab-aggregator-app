package cab.app.paymentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PayRequest {
    @NotNull(message = "Ride ID cannot be null")
    private Long rideId;

    @NotNull(message = "Passenger ID cannot be null")
    private Long passengerId;
}
