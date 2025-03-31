package cab.app.paymentservice.integration;

import cab.app.paymentservice.dto.request.DepositRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
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
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(
        statements = {
                TRUNCATE_PASSENGER_BALANCE,
                INSERT_PASSENGER_BALANCE
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class PassengerBalanceIntegrationTest {
    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private DepositRequest depositRequest;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        depositRequest = new DepositRequest(DEPOSIT_AMOUNT);

    }

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Test
    void testDepositOnPassengerBalance() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(depositRequest)
                .when()
                .patch(PASSENGER_BALANCE_URL + "/" + PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testDepositOnInvalidPassengerBalance() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(depositRequest)
                .when()
                .patch(PASSENGER_BALANCE_URL + "/" + WRONG_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(1)
    void testGetPassengerBalance() {
        given()
                .when()
                .get(PASSENGER_BALANCE_URL + "/" + PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("balance", equalTo(BALANCE_AMOUNT.floatValue()));
    }

    @Test
    void testGetInvalidPassengerBalance() {
        given()
                .when()
                .get(PASSENGER_BALANCE_URL + "/" + WRONG_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
