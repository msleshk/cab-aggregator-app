package cab.app.ratingservice.e2e;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static cab.app.ratingservice.utils.TestConstants.*;
import static io.restassured.RestAssured.given;

public class CucumberSteps {

    private static final String BASE = "http://localhost:8083";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;
    private RatingRequest ratingRequest;
    private RatingToUpdate ratingUpdateRequest;

    @When("get rating by id {string}")
    public void getRatingById(String id) {
        response = given()
                .pathParam("id", id)
                .when()
                .get(BASE + GET_RATING_BY_ID);
    }

    @Then("response status is {int}")
    public void responseStatusIs(int status) {
        response.then().statusCode(status);
    }

    @When("get all ratings with offset {int} and limit {int}")
    public void getAllRatingsWithOffsetAndLimit(int offset, int limit) {
        response = given()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(BASE + BASE_URL);
    }

    @When("get average rating for user {long} with role {string}")
    public void getAverageRatingForUser(Long userId, String role) {
        response = given()
                .pathParam("id", userId)
                .pathParam("role", role)
                .when()
                .get(BASE + GET_AVG_RATING);
    }

    @When("get rating by ride id {long}")
    public void getRatingByRideId(Long rideId) {
        response = given()
                .pathParam("id", rideId)
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(BASE + GET_RATING_BY_RIDE);
    }

    @When("get rating by user id {long} and role {string}")
    public void getRatingByUserIdAndRole(Long userId, String role) {
        response = given()
                .pathParam("id", userId)
                .pathParam("role", role)
                .queryParam("offset", OFFSET)
                .queryParam("limit", LIMIT)
                .when()
                .get(BASE + GET_RATING_BY_USER_ROLE);
    }

    @Given("create new rating body")
    public void createNewRatingBody(String body) throws Exception {
        ratingRequest = objectMapper.readValue(body, RatingRequest.class);
    }

    @When("create new rating")
    public void createNewRating() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ratingRequest)
                .when()
                .post(BASE + CREATE_RATING);
    }

    @Given("update rating body")
    public void updateRatingBody(String body) throws Exception {
        ratingUpdateRequest = objectMapper.readValue(body, RatingToUpdate.class);
    }

    @When("update rating with id {string}")
    public void updateRatingWithId(String id) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", id)
                .body(ratingUpdateRequest)
                .when()
                .patch(BASE + UPDATE_RATING);
    }

    @When("delete rating with id {string}")
    public void deleteRatingWithId(String id) {
        response = given()
                .pathParam("id", id)
                .when()
                .delete(BASE + DELETE_RATING);
    }
}
