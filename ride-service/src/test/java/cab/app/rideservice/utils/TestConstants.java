package cab.app.rideservice.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestConstants {
    // CostCalculator constants
    public static final BigDecimal BASE_FARE = BigDecimal.valueOf(5.00);
    public static final BigDecimal COST_PER_KM = BigDecimal.valueOf(1.50);
    public static final BigDecimal LONG_TRIP_MULTIPLIER = BigDecimal.valueOf(1.2);

    public static final BigDecimal SHORT_TRIP_DISTANCE = BigDecimal.valueOf(10);
    public static final BigDecimal LONG_TRIP_DISTANCE = BigDecimal.valueOf(25);
    public static final BigDecimal NEGATIVE_DISTANCE = BigDecimal.valueOf(-5);

    public static final String ERROR_MESSAGE_INVALID_DISTANCE = "Distance must be greater than zero";

    // RideStatusValidator constants
    public static final String ERROR_STATUS_REQUESTED = "Status can only be changed to ACCEPTED or CANCELLED!";
    public static final String ERROR_STATUS_ACCEPTED = "Status can only be changed to IN_PROGRESS or CANCELLED!";
    public static final String ERROR_STATUS_IN_PROGRESS = "Status can only be changed to COMPLETED or CANCELLED!";
    public static final String ERROR_STATUS_FINAL = "Status cannot be changed!";

    // RideServiceImpl test constants
    public static final Long RIDE_ID = 1L;
    public static final Long USER_ID = 1L;
    public static final Long DRIVER_ID = 2L;
    public static final Long CAR_ID = 2L;
    public static final Long INVALID_USER_ID = -1L;
    public static final String DEPARTURE_ADDRESS = "Start";
    public static final String ARRIVAL_ADDRESS = "End";
    public static final String NEW_DEPARTURE_ADDRESS = "Start";
    public static final String NEW_ARRIVAL_ADDRESS = "End";
    public static final String STATUS_REQUESTED = "REQUESTED";
    public static final String RIDE_STATUS_COMPLETED = "COMPLETED";
    public static final String RIDE_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    public static final String ORDER_DATE_TIME_FIELD = "orderDateTime";
    public static final String COST_FIELD = "cost";

    // Pagination constants
    public static final int OFFSET = 0;
    public static final int LIMIT = 10;

    // API
    public static final String BASE_URL = "/api/v1/rides";
    public static final String CREATE_RIDE = BASE_URL;
    public static final String SET_DRIVER = BASE_URL + "/{id}/set-driver/{driverId}";
    public static final String UPDATE_RIDE = BASE_URL + "/{id}";
    public static final String UPDATE_RIDE_STATUS = BASE_URL +"/{id}/{status}";
    public static final String DELETE_RIDE = BASE_URL + "/{id}";
    public static final String GET_RIDE_BY_ID = BASE_URL + "/{id}";
    public static final String GET_RIDES_BY_DRIVER_ID = BASE_URL + "/rides-by-driver/{id}";
    public static final String GET_RIDES_BY_STATUS = BASE_URL + "/rides-by-status/{status}";
    public static final String GET_RIDES_BY_PASSENGER_ID = BASE_URL + "/rides-by-passenger/{id}";

}

