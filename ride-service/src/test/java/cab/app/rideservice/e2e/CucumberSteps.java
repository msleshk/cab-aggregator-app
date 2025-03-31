package cab.app.rideservice.e2e;

import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.request.RideToUpdate;
import cab.app.rideservice.dto.response.RideResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CucumberSteps {

    private static final String BASE_URL = "http://localhost:8082/api/v1/rides";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;
    private RideRequest rideRequest;

    @Given("create new ride body")
    public void createNewRideBody(String body) throws Exception {
        rideRequest = objectMapper.readValue(body, RideRequest.class);
    }

    @When("create new ride")
    public void createNewRide() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(rideRequest)
                .when()
                .post(BASE_URL);
    }

    @Then("response status is {int}")
    public void responseStatusIs(int status) {
        response.then().statusCode(status);
    }

    @When("get ride with id {long}")
    public void getRideWithId(Long id) {
        response = given()
                .when()
                .get(BASE_URL + "/" + id);
    }

    @And("response body contains ride details")
    public void responseBodyContainsRideDetails(String expectedBody) throws Exception {
        RideResponse expectedResponse = objectMapper.readValue(expectedBody, RideResponse.class);
        RideResponse actualResponse = response.as(RideResponse.class);

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "driverId", "passengerId", "departureAddress", "arrivalAddress", "status")
                .isEqualTo(expectedResponse);

    }

    @When("delete ride with id {long}")
    public void deleteRideWithId(Long id) {
        response = given()
                .when()
                .delete(BASE_URL + "/" + id);
    }

    @When("get all rides with offset {int} and limit {int}")
    public void getAllRidesWithOffsetAndLimit(int offset, int limit) {
        response = given()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(BASE_URL);
    }

    @When("update ride with id {long}")
    public void updateRideWithId(Long id, String body) throws Exception {
        RideToUpdate rideToUpdateRequest = objectMapper.readValue(body, RideToUpdate.class);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(rideToUpdateRequest)
                .when()
                .patch(BASE_URL + "/" + id);
    }

    @When("update ride status for ride {long} to {string}")
    public void updateRideStatusForRide(Long rideId, String status) {
        response = given()
                .when()
                .patch(BASE_URL + "/" + rideId + "/" + status);
    }

}

