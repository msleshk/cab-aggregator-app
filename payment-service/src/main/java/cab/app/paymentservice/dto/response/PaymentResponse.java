package cab.app.paymentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long rideId;
    private Long driverId;
    private Long passengerId;
    private String status;
    private BigDecimal cost;
    private BigDecimal costAfterPromoCode;
}
