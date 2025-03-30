package cab.app.ratingservice.utils;

import java.time.LocalDateTime;

public class TestConstants {
    public static final String RATING_ID = "rating123";
    public static final Long USER_ID = 1L;
    public static final Long RATED_USER_ID = 2L;
    public static final Long WRONG_USER_ID = 999L;
    public static final Long RIDE_ID = 10L;
    public static final String DRIVER_ROLE = "DRIVER";
    public static final String PASSENGER_ROLE = "PASSENGER";
    public static final int RATING_VALUE = 5;
    public static final String COMMENT = "Great ride!";
    public static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    public static final int OFFSET = 0;
    public static final int LIMIT = 10;

    // Updated rating data
    public static final int UPDATED_RATING_VALUE = 4;
    public static final String UPDATED_COMMENT = "Good ride, but could be better.";

    // Ride status constants
    public static final String RIDE_STATUS_COMPLETED = "COMPLETED";
    public static final String RIDE_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String ROLE = "DRIVER";
    public static final int SCORE = 4;
    public static final int UPDATED_SCORE = 5;
    public static final double AVERAGE_SCORE = 4.7;

    // API Endpoints
    public static final String BASE_URL = "/api/v1/rating";
    public static final String GET_AVG_RATING = BASE_URL + "/avg-rating/{id}/{role}";
    public static final String GET_RATING_BY_RIDE = BASE_URL + "/ride/{id}";
    public static final String GET_RATING_BY_USER_ROLE = BASE_URL + "/user/{id}/{role}";
    public static final String GET_RATING_BY_ID = BASE_URL + "/{id}";
    public static final String CREATE_RATING = BASE_URL;
    public static final String UPDATE_RATING = BASE_URL + "/{id}";
    public static final String DELETE_RATING = BASE_URL + "/{id}";
}
