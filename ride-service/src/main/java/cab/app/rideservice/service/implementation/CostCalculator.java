package cab.app.rideservice.service.implementation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CostCalculator {
    private static final BigDecimal BASE_FARE = BigDecimal.valueOf(5.00);
    private static final BigDecimal COST_PER_KM = BigDecimal.valueOf(1.50);
    private static final BigDecimal LONG_TRIP_THRESHOLD = BigDecimal.valueOf(20);
    private static final BigDecimal LONG_TRIP_MULTIPLIER = BigDecimal.valueOf(1.2);

    public BigDecimal generateCost(BigDecimal distance) {
        if (distance == null || distance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Distance must be greater than zero");
        }

        BigDecimal cost = BASE_FARE.add(COST_PER_KM.multiply(distance));

        if (distance.compareTo(LONG_TRIP_THRESHOLD) > 0) {
            cost = cost.multiply(LONG_TRIP_MULTIPLIER);
        }

        return cost.setScale(2, RoundingMode.HALF_UP);
    }
}
