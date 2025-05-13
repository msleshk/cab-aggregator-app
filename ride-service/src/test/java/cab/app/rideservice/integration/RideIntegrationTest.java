package cab.app.rideservice.integration;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.request.RideToUpdate;
import cab.app.rideservice.dto.response.DriverResponse;
import cab.app.rideservice.kafka.KafkaProducer;
import cab.app.rideservice.kafka.KafkaProducerConfig;
import cab.app.rideservice.kafka.KafkaTopic;
import cab.app.rideservice.model.enums.DriverStatus;
import cab.app.rideservice.service.implementation.EntityValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static cab.app.rideservice.utils.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockBean({KafkaProducer.class, KafkaProducerConfig.class, KafkaTopic.class, KafkaTemplate.class})
public class RideIntegrationTest {
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

    @MockBean
    private EntityValidator validator;

    private RideRequest rideRequest;
    private RideRequest invalidRequest;
    private RideToUpdate rideToUpdate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        invalidRequest = new RideRequest(INVALID_USER_ID, DEPARTURE_ADDRESS, ARRIVAL_ADDRESS, NEGATIVE_DISTANCE);
        rideRequest = new RideRequest(USER_ID, DEPARTURE_ADDRESS, ARRIVAL_ADDRESS, SHORT_TRIP_DISTANCE);
        rideToUpdate = new RideToUpdate(NEW_DEPARTURE_ADDRESS, NEW_ARRIVAL_ADDRESS, LONG_TRIP_DISTANCE);
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
    void testAddRide() throws Exception {
        doNothing().when(validator).checkIfPassengerExist(any());

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(rideRequest))
                .when()
                .post(CREATE_RIDE)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void testAddInvalidRide() throws Exception {
        doNothing().when(validator).checkIfPassengerExist(any());

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(invalidRequest))
                .when()
                .post(CREATE_RIDE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(2)
    void testGetRideById() {
        String response =
                given()
                        .when()
                        .get(GET_RIDE_BY_ID, RIDE_ID)
                        .then()
                        .extract()
                        .asString();

        System.out.println("Response JSON: " + response);
        given()
                .when()
                .get(GET_RIDE_BY_ID, RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(3)
    void testUpdateRide() throws Exception {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(rideToUpdate))
                .when()
                .patch(UPDATE_RIDE, RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(7)
    void testDeleteRide() {
        given()
                .when()
                .delete(DELETE_RIDE, RIDE_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(4)
    void testAssignDriver() {
        DriverResponse driverResponse = new DriverResponse(DRIVER_ID, DriverStatus.AVAILABLE, CAR_ID);

        when(validator.getDriverById(any())).thenReturn(driverResponse);

        given()
                .when()
                .patch(SET_DRIVER, RIDE_ID, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(5)
    void testUpdateRideStatus() {
        given()
                .when()
                .patch(UPDATE_RIDE_STATUS, RIDE_ID, RIDE_STATUS_IN_PROGRESS)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(6)
    void testGetRidesByPassengerId() {
        given()
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(GET_RIDES_BY_PASSENGER_ID, USER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(6)
    void testGetRidesByDriverId() {
        given()
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(GET_RIDES_BY_DRIVER_ID, DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(4)
    void testGetRidesByStatus() {
        given()
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(GET_RIDES_BY_STATUS, STATUS_REQUESTED)
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}

