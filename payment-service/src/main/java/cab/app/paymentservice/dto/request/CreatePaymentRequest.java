package cab.app.paymentservice.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
    @NotNull(message = "Ride ID cannot be null")
    private Long rideId;

    @NotNull(message = "Passenger ID cannot be null")
    private Long passengerId;

    @NotNull(message = "Driver ID cannot be null")
    private Long driverId;

    @NotNull(message = "Cost cannot be null")
    @Min(value = 1, message = "Cost must be at least 1")
    private BigDecimal cost;

    private String promoCode;
}
