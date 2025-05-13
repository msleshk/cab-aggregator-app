package com.example.passengerservice.utils;

public class TestConstants {
    public static final Long PASSENGER_ID = 1L;
    public static final String PASSENGER_NAME = "John Doe";
    public static final String PASSENGER_EMAIL = "johndoe@example.com";
    public static final String PASSENGER_PHONE = "+375(29)123-45-67";
    public static final Double AVERAGE_RATING = 4.0;

    public static final String NEW_PASSENGER_NAME = "Jane Doe";
    public static final String NEW_PASSENGER_EMAIL = "janedoe@example.com";
    public static final String NEW_PASSENGER_PHONE = "+375(29)765-43-21";
    public static final String INVALID_PASS_EMAIL = "johndoeexample.com";
    public static final String INVALID_PASS_PHONE = "+375(29)1230000";

    public static final Long WRONG_ID = 999L;
    public static final Integer OFFSET = 0;
    public static final Integer LIMIT = 5;

    public static final String EXCEPTION_MESSAGE_NOT_FOUND = "No such passenger!";
    public static final String EXCEPTION_MESSAGE_EMAIL_TAKEN = "Email already been taken!";
    public static final String EXCEPTION_MESSAGE_PHONE_TAKEN = "Passenger with this phone number already exist!";

    public static final String PASSENGER_BASE_URL = "/api/v1/passengers";
    public static final String PASSENGER_ID_URL = PASSENGER_BASE_URL + "/{id}";
}
