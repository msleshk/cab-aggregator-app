package com.example.driverservice.utils;

public class TestConstants {
    // Driver
    public static final Long DRIVER_ID = 1L;
    public static final String DRIVER_NAME = "John Doe";
    public static final String DRIVER_EMAIL = "johndoe@example.com";
    public static final String DRIVER_PHONE = "+375(29)123-45-67";
    public static final Double AVERAGE_RATING = 4.0;

    // Updated driver
    public static final String NEW_DRIVER_NAME = "John Doe Update";
    public static final String NEW_DRIVER_EMAIL = "johndoeupdated@example.com";
    public static final String NEW_DRIVER_PHONE = "+375(29)123-00-00";
    public static final String INVALID_DRIVER_EMAIL = "johndoeexample.com";
    public static final String INVALID_DRIVER_PHONE = "+375(29)1230000";

    public static final Long WRONG_ID = 999L;
    public static final Integer OFFSET = 0;
    public static final Integer LIMIT = 5;

    // Car
    public static final Long CAR_ID = 1L;
    public static final String NEW_CAR_NUMBER = "0000XX-0";
    public static final String INVALID_CAR_NUMBER = "00000XXX-0";
    public static final String CAR_NUMBER = "1234AA-1";
    public static final String COLOR = "Black";
    public static final String BRAND = "Toyota";
    public static final String MODEL = "RAV4";

    // Api
    public static final String CAR_BASE_URL = "/api/v1/cars";
    public static final String CAR_BY_NUMBER_URL = "/api/v1/cars/car-by-number";
    public static final String CAR_ID_URL = CAR_BASE_URL + "/{id}";
    public static final String DRIVER_BASE_URL = "/api/v1/drivers";
    public static final String DRIVER_ID_URL = DRIVER_BASE_URL + "/{id}";
}
