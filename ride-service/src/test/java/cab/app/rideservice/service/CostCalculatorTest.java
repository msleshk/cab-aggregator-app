package cab.app.rideservice.service;

import cab.app.rideservice.service.implementation.CostCalculator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static cab.app.rideservice.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class CostCalculatorTest {

    private final CostCalculator costCalculator = new CostCalculator();

    @Test
    void shouldCalculateCostForShortTrip() {
        BigDecimal expectedCost = BASE_FARE.add(COST_PER_KM.multiply(SHORT_TRIP_DISTANCE));

        BigDecimal actualCost = costCalculator.generateCost(SHORT_TRIP_DISTANCE);

        assertEquals(expectedCost.setScale(2, RoundingMode.HALF_UP), actualCost);
    }

    @Test
    void shouldCalculateCostForLongTrip() {
        BigDecimal baseCost = BASE_FARE.add(COST_PER_KM.multiply(LONG_TRIP_DISTANCE));
        BigDecimal expectedCost = baseCost.multiply(LONG_TRIP_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);

        BigDecimal actualCost = costCalculator.generateCost(LONG_TRIP_DISTANCE);

        assertEquals(expectedCost, actualCost);
    }

    @Test
    void shouldThrowExceptionForZeroDistance() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> costCalculator.generateCost(BigDecimal.ZERO)
        );
        assertEquals(ERROR_MESSAGE_INVALID_DISTANCE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNegativeDistance() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> costCalculator.generateCost(NEGATIVE_DISTANCE)
        );
        assertEquals(ERROR_MESSAGE_INVALID_DISTANCE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullDistance() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> costCalculator.generateCost(null)
        );
        assertEquals(ERROR_MESSAGE_INVALID_DISTANCE, exception.getMessage());
    }
}


