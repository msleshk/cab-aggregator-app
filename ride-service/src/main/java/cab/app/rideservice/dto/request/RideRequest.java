package cab.app.rideservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RideRequest {
    @Positive(message = "Driver id cannot be less than 0!")
    private Long driverId;
    @NotNull(message = "Passenger id cannot not be null!")
    @Positive(message = "Passenger id cannot be less than 0!")
    private Long passengerId;
    @NotNull(message = "Departure address cannot not be null!")
    private String departureAddress;
    @NotNull(message = "Arrival address cannot be null!")
    private String arrivalAddress;
    @NotNull(message = "Distance cannot be null!")
    @Positive(message = "Distance cannot be less than 0!")
    private BigDecimal distance;
}
