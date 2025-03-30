package cab.app.paymentservice.integration;

import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static cab.app.paymentservice.utils.TestConstants.*;
import static cab.app.paymentservice.utils.TestConstants.PASSENGER_ID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentIntegrationTest {
    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private ObjectMapper objectMapper;
    private CreatePaymentRequest createPaymentRequest;
    private CreatePaymentRequest invalidRequest;
    private PayRequest payRequest;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        createPaymentRequest = CreatePaymentRequest.builder()
                .rideId(RIDE_ID)
                .passengerId(PASSENGER_ID)
                .driverId(DRIVER_ID)
                .cost(AMOUNT)
                .build();
        invalidRequest = CreatePaymentRequest.builder()
                .rideId(null)
                .passengerId(null)
                .driverId(null)
                .cost(AMOUNT)
                .build();
        payRequest = PayRequest.builder()
                .rideId(RIDE_ID)
                .passengerId(PASSENGER_ID)
                .promoCode(null)
                .build();
    }

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Test
    @Order(1)
    void testAddPayment() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createPaymentRequest)
                .when()
                .post(PAYMENT_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void testAddInvalidPayment() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invalidRequest)
                .when()
                .post(PAYMENT_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(2)
    void testAddAlreadyCreatedPayment() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createPaymentRequest)
                .when()
                .post(PAYMENT_URL)
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @Order(3)
    void testGetAllPayments() {
        given()
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(PAYMENT_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("responseList", hasSize(1));
    }

    @Test
    void testGetAllPaymentsWithoutOffsetLimit() {
        given()
                .when()
                .get(PAYMENT_URL)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @Order(4)
    @Sql(
            statements = {
                    INSERT_PASSENGER_BALANCE
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testPayForRideWithInsufficientMoney() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(payRequest)
                .when()
                .patch(PAYMENT_URL + "/pay")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(EXCEPTION_INSUFFICIENT_BALANCE));
    }

    @Test
    @Order(5)
    @Sql(
            statements = {
                    UPDATE_PASSENGER_BALANCE,
                    INSERT_DRIVER_BALANCE
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testPayForRide() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(payRequest)
                .when()
                .patch(PAYMENT_URL + "/pay")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo(PAID_STATUS));
    }

    @Test
    @Order(5)
    void testGetPaymentById() {
        given()
                .when()
                .get(PAYMENT_URL + "/" + PAYMENT_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(PAYMENT_ID.intValue()));
    }

    @Test
    @Order(6)
    void testGetPaymentByRideId() {
        given()
                .when()
                .get(PAYMENT_URL + "/ride/" + RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rideId", equalTo(RIDE_ID.intValue()));
    }

    @Test
    @Order(7)
    void testDeletePayment() {
        given()
                .when()
                .delete(PAYMENT_URL + "/" + PAYMENT_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(8)
    void testGetDeletedPaymentById() {
        given()
                .when()
                .get(PAYMENT_URL + "/" + PAYMENT_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
