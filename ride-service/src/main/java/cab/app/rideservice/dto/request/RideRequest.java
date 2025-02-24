package cab.app.rideservice.dto.request;

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
    private Long driverId;
    private Long passengerId;
    private String departureAddress;
    private String arrivalAddress;
    private BigDecimal distance;
}
