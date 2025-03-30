package cab.app.paymentservice.utils;

import java.math.BigDecimal;

public class TestConstants {
    public static final Long DRIVER_ID = 1L;
    public static final Long PASSENGER_ID = 1L;
    public static final Long PAYMENT_ID = 1L;
    public static final Long RIDE_ID = 3L;
    public static final Long WRONG_ID = 99L;
    public static final BigDecimal INITIAL_BALANCE = BigDecimal.ZERO;
    public static final BigDecimal BALANCE_AMOUNT = new BigDecimal("50.00");
    public static final BigDecimal EXCESS_AMOUNT = new BigDecimal("1000.00");

    // Exception messages
    public static final String EXCEPTION_BALANCE_NOT_FOUND = "Driver balance not found!";
    public static final String EXCEPTION_INSUFFICIENT_BALANCE = "Insufficient money on balance!";

    // API
    public static final String DRIVER_BALANCE_URL = "/api/v1/driver-balance";
    public static final String PASSENGER_BALANCE_URL = "/api/v1/passenger-balance";
    public static final String PAYOUT_URL = "/api/v1/payout";
    public static final BigDecimal DEPOSIT_AMOUNT = new BigDecimal("20.00");
    public static final String PAYMENT_URL = "/api/v1/payment";
    public static final String PAID_STATUS = "PAID";
    public static final int OFFSET = 0;
    public static final int LIMIT = 10;
    public static final BigDecimal AMOUNT = new BigDecimal("150.00");

    public static final String TRUNCATE_DRIVER_BALANCE = "TRUNCATE TABLE driver_balance CASCADE";
    public static final String INSERT_DRIVER_BALANCE = "INSERT INTO driver_balance (driver_id, balance) VALUES (1, 50.00)";
    public static final String TRUNCATE_PASSENGER_BALANCE = "TRUNCATE TABLE passenger_balance CASCADE";
    public static final String INSERT_PASSENGER_BALANCE = "INSERT INTO passenger_balance (passenger_id, balance) VALUES (1, 50.00)";
    public static final String UPDATE_PASSENGER_BALANCE = "UPDATE passenger_balance SET balance = 150 WHERE passenger_id = 1";
    public static final String UPDATE_DRIVER_BALANCE = "UPDATE driver_balance SET balance = 0 WHERE driver_id = 1";
}
