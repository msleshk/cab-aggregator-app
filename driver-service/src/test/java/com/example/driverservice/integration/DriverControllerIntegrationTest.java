package com.example.driverservice.integration;

import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.kafka.producer.KafkaProducerConfig;
import com.example.driverservice.kafka.producer.KafkaTopic;
import com.example.driverservice.model.enums.Gender;
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

import static com.example.driverservice.utils.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockBean({KafkaProducer.class, KafkaProducerConfig.class, KafkaTopic.class, KafkaTemplate.class})
public class DriverControllerIntegrationTest {

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
    void testAddDriver() throws Exception {
        DriverRequest driverRequest = new DriverRequest(DRIVER_NAME, DRIVER_EMAIL, DRIVER_PHONE, Gender.MALE, null);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(driverRequest))
                .when()
                .post(DRIVER_BASE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @Order(2)
    void testGetAllDrivers() {
        given()
                .queryParam("offset", 0)
                .queryParam("limit", 5)
                .when()
                .get(DRIVER_BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("responseList", hasSize(1));
    }

    @Test
    @Order(3)
    void testGetDriverById() {
        given()
                .when()
                .get(DRIVER_BASE_URL + "/" + DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("name", equalTo(DRIVER_NAME));
    }

    @Test
    @Order(4)
    void testUpdateDriver() throws Exception {
        DriverRequest updateRequest = new DriverRequest(NEW_DRIVER_NAME, NEW_DRIVER_EMAIL, NEW_DRIVER_PHONE, Gender.MALE, null);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(updateRequest))
                .when()
                .patch(DRIVER_BASE_URL + "/" + DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(6)
    void testDeleteDriver() {
        given()
                .when()
                .delete(DRIVER_BASE_URL + "/" + DRIVER_ID)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Order(5)
    void testAddExistingDriver() throws Exception {
        DriverRequest driverRequest = new DriverRequest(DRIVER_NAME, NEW_DRIVER_EMAIL, NEW_DRIVER_PHONE, Gender.MALE, null);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(driverRequest))
                .when()
                .post(DRIVER_BASE_URL)
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void testGetNotExistingDriver() {
        given()
                .when()
                .get(DRIVER_BASE_URL + "/" + DRIVER_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testAddInvalidDriver() throws Exception {
        DriverRequest driverRequest = new DriverRequest(DRIVER_NAME, INVALID_DRIVER_EMAIL, INVALID_DRIVER_PHONE, Gender.MALE, null);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(driverRequest))
                .when()
                .post(DRIVER_BASE_URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}


