package cab.app.paymentservice.dto.response;


import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long rideId,
        Long driverId,
        Long passengerId,
        String status,
        BigDecimal cost,
        BigDecimal costAfterPromoCode
) {
}
