package cab.app.paymentservice.integration;

import cab.app.paymentservice.dto.request.PayoutRequest;
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
import static cab.app.paymentservice.utils.TestConstants.BALANCE_AMOUNT;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(
        statements = {
                TRUNCATE_DRIVER_BALANCE,
                INSERT_DRIVER_BALANCE
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class PayoutIntegrationTest {
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
    private PayoutRequest payoutRequest;
    private PayoutRequest invalidPayoutRequest;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        payoutRequest = PayoutRequest.builder()
                .driverId(DRIVER_ID)
                .amount(BALANCE_AMOUNT)
                .build();

        invalidPayoutRequest = PayoutRequest.builder()
                .driverId(WRONG_ID)
                .amount(BALANCE_AMOUNT)
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
    void testGetMoneyFromBalance() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(payoutRequest)
                .when()
                .post(PAYOUT_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", equalTo(BALANCE_AMOUNT.floatValue()));
    }
    @Test
    @Order(2)
    void testGetMoneyFromInvalidBalance() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invalidPayoutRequest)
                .when()
                .post(PAYOUT_URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
    @Sql(
            statements = {
                    UPDATE_DRIVER_BALANCE
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Test
    @Order(3)
    void testGetMoneyFromInsufficientBalance() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(payoutRequest)
                .when()
                .post(PAYOUT_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(EXCEPTION_INSUFFICIENT_BALANCE));
    }
}
