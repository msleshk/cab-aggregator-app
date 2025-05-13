package com.example.passengerservice.integration;

import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.kafka.producer.KafkaProducerConfig;
import com.example.passengerservice.kafka.producer.KafkaTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.example.passengerservice.utils.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockBean({KafkaProducer.class, KafkaProducerConfig.class, KafkaTopic.class, KafkaTemplate.class})
public class PassengerControllerIntegrationTest {
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

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

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
    void testAddPassenger() throws Exception {
        PassengerRequest passengerRequest = new PassengerRequest(PASSENGER_NAME, PASSENGER_EMAIL, PASSENGER_PHONE);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(passengerRequest))
                .when()
                .post(PASSENGER_BASE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @Order(2)
    void testGetAllPassengers() {
        given()
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(PASSENGER_BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("responseList", hasSize(1));
    }

    @Test
    @Order(3)
    void testGetPassengerById() {
        given()
                .when()
                .get(PASSENGER_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("name", equalTo(PASSENGER_NAME));
    }

    @Test
    @Order(4)
    void testUpdatePassenger() throws Exception {
        PassengerRequest updateRequest = new PassengerRequest(NEW_PASSENGER_NAME, NEW_PASSENGER_EMAIL, NEW_PASSENGER_PHONE);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(updateRequest))
                .when()
                .patch(PASSENGER_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(5)
    void testAddExistingPassenger() throws Exception {
        PassengerRequest passengerRequest = new PassengerRequest(PASSENGER_NAME, NEW_PASSENGER_EMAIL, NEW_PASSENGER_PHONE);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(passengerRequest))
                .when()
                .post(PASSENGER_BASE_URL)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", equalTo(EXCEPTION_MESSAGE_EMAIL_TAKEN));
    }

    @Test
    @Order(6)
    void testDeletePassenger() {
        given()
                .when()
                .delete(PASSENGER_ID_URL, PASSENGER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testGetNotExistingPassenger() {
        given()
                .when()
                .get(PASSENGER_ID_URL, WRONG_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(EXCEPTION_MESSAGE_NOT_FOUND));
    }

    @Test
    void testAddInvalidPassenger() throws Exception {
        PassengerRequest passengerRequest = new PassengerRequest(PASSENGER_NAME, INVALID_PASS_EMAIL, INVALID_PASS_PHONE);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(passengerRequest))
                .when()
                .post(PASSENGER_BASE_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}

