package cab.app.rideservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RideResponse {
    private Long id;
    private Long driverId;
    private Long passengerId;
    private String departureAddress;
    private String arrivalAddress;
    private String status;
    private LocalDateTime orderDateTime;
    private BigDecimal cost;
}
