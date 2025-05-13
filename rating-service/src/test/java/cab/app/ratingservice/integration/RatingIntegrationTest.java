package cab.app.ratingservice.integration;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.ride.RideResponse;
import cab.app.ratingservice.kafka.KafkaProducerConfig;
import cab.app.ratingservice.kafka.KafkaTopic;
import cab.app.ratingservice.model.Rating;
import cab.app.ratingservice.model.enums.Role;
import cab.app.ratingservice.service.implementation.EntityValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static cab.app.ratingservice.utils.TestConstants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@MockBean({KafkaProducer.class, KafkaProducerConfig.class, KafkaTopic.class, KafkaTemplate.class})
public class RatingIntegrationTest {
    @LocalServerPort
    private int port;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0")
            .withReuse(true);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @MockBean
    private EntityValidator validator;

    private RideResponse rideResponse;
    private String savedRatingId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        mongoTemplate.getDb().drop();
        seedTestData();
        rideResponse = new RideResponse(RIDE_ID, RATED_USER_ID, USER_ID, RIDE_STATUS_COMPLETED);
        savedRatingId = fetchSavedRatingId();
    }

    private String fetchSavedRatingId() {
        List<Rating> ratings = mongoTemplate.findAll(Rating.class);
        return ratings.isEmpty() ? null : ratings.get(0).getId();
    }

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    private void seedTestData() {
        Rating rating = new Rating();
        rating.setRideId(RIDE_ID);
        rating.setUserId(USER_ID);
        rating.setRatedUserId(RATED_USER_ID);
        rating.setRating(RATING_VALUE);
        rating.setComment(COMMENT);
        rating.setUserRole(Role.PASSENGER);
        rating.setRatedUserRole(Role.DRIVER);
        rating.setCreatedAt(LocalDateTime.now());
        mongoTemplate.save(rating);
    }

    @Test
    void testAddRating() throws Exception {
        RatingRequest request = new RatingRequest(RIDE_ID, RATED_USER_ID, Role.DRIVER.toString(), RATING_VALUE, COMMENT);

        doNothing().when(validator).checkIfUserExist(request.userId(), Role.valueOf(request.userRole()));
        when(validator.getRideById(request.rideId())).thenReturn(rideResponse);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(CREATE_RATING)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void testAddInvalidRating() throws Exception {
        RatingRequest request = new RatingRequest(RIDE_ID, WRONG_USER_ID, DRIVER_ROLE, RATING_VALUE, COMMENT);

        doNothing().when(validator).checkIfUserExist(request.userId(), Role.valueOf(request.userRole()));
        when(validator.getRideById(request.rideId())).thenReturn(rideResponse);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(CREATE_RATING)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void testAddExistingRating() throws Exception {
        RatingRequest request = new RatingRequest(RIDE_ID, USER_ID, Role.PASSENGER.toString(), 5, "Хорошая поездка");

        doNothing().when(validator).checkIfUserExist(request.userId(), Role.valueOf(request.userRole()));
        when(validator.getRideById(request.rideId())).thenReturn(rideResponse);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(CREATE_RATING)
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void testGetAllRatings() {
        given()
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("responseList", not(empty()));
    }

    @Test
    void testGetAverageRatingByUser() {
        given()
                .pathParam("id", USER_ID)
                .pathParam("role", Role.DRIVER.toString())
                .when()
                .get(GET_AVG_RATING)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void testGetRatingByRideId() {
        given()
                .pathParam("id", RIDE_ID)
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(GET_RATING_BY_RIDE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("responseList", not(empty()));
    }

    @Test
    void testGetRatingByUserAndRole() {
        given()
                .pathParam("id", USER_ID)
                .pathParam("role", Role.PASSENGER.toString())
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(GET_RATING_BY_USER_ROLE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("responseList", not(empty()));
    }

    @Test
    void testGetRatingById() {

        given()
                .pathParam("id", savedRatingId)
                .when()
                .get(GET_RATING_BY_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void testUpdateRating() throws Exception {
        RatingToUpdate update = new RatingToUpdate(UPDATED_RATING_VALUE, UPDATED_COMMENT);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", savedRatingId)
                .body(objectMapper.writeValueAsString(update))
                .when()
                .patch(UPDATE_RATING)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testDeleteRating() {
        given()
                .pathParam("id", savedRatingId)
                .when()
                .delete(DELETE_RATING)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}
