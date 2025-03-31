package com.example.driverservice.integration;

import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.kafka.producer.KafkaProducerConfig;
import com.example.driverservice.kafka.producer.KafkaTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.example.driverservice.utils.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockBean({KafkaProducer.class, KafkaProducerConfig.class, KafkaTopic.class, KafkaTemplate.class})
public class CarControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    ObjectMapper objectMapper;

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
    void testAddCar() throws Exception {
        CarRequest carRequest = new CarRequest(BRAND, MODEL, COLOR, CAR_NUMBER);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(carRequest))
                .when()
                .post(CAR_BASE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @Order(7)
    void testDeleteCar() {
        given()
                .when()
                .delete(CAR_BASE_URL + "/" + CAR_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(2)
    void testUpdateCar() throws Exception {
        CarRequest carToUpdate = new CarRequest(BRAND, MODEL, COLOR, NEW_CAR_NUMBER);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(carToUpdate))
                .when()
                .patch(CAR_BASE_URL + "/" + CAR_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(3)
    void testGetAllCars() {
        given()
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(CAR_BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("responseList", hasSize(1));
    }

    @Test
    @Order(4)
    void testGetCarById() {
        given()
                .when()
                .get(CAR_BASE_URL + "/" + CAR_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("carNumber", equalTo(NEW_CAR_NUMBER));

    }

    @Test
    @Order(5)
    void testGetCarByCarNumber() {
        given()
                .when()
                .get(CAR_BY_NUMBER_URL + "/" + NEW_CAR_NUMBER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("carNumber", equalTo(NEW_CAR_NUMBER));

    }

    @Test
    @Order(6)
    void testAddExistingCar() throws Exception {
        CarRequest carRequest = new CarRequest(BRAND, MODEL, COLOR, NEW_CAR_NUMBER);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(carRequest))
                .when()
                .post(CAR_BASE_URL)
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void testAddNotValidCar() throws Exception {
        CarRequest carRequest = new CarRequest(BRAND, MODEL, COLOR, INVALID_CAR_NUMBER);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(carRequest))
                .when()
                .post(CAR_BASE_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testGetNotExistingCar() {
        given()
                .when()
                .get(CAR_BASE_URL + "/" + CAR_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
