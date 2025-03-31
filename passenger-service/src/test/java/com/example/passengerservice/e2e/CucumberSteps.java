package com.example.passengerservice.e2e;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static com.example.passengerservice.utils.TestConstants.PASSENGER_BASE_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CucumberSteps {
    private Response response;
    private static final String BASE_URL = "http://localhost:8080" + PASSENGER_BASE_URL;

    @When("send POST request to add a passenger with body:")
    public void addNewPassenger(String requestBody) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(BASE_URL);
    }

    @When("send GET request to get all passengers with offset {int} and limit {int}")
    public void getAllPassengers(int offset, int limit) {
        response = given()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(BASE_URL);
    }

    @When("send GET request to get passenger by ID {long}")
    public void getPassengerById(Long id) {
        response = given()
                .when()
                .get(BASE_URL + "/" + id);
    }

    @When("send PATCH request to update passenger with ID {long} and body:")
    public void updatePassenger(Long id, String requestBody) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .patch(BASE_URL + "/" + id);
    }

    @When("send DELETE request to delete passenger with ID {long}")
    public void deletePassenger(Long id) {
        response = given()
                .when()
                .delete(BASE_URL + "/" + id);
    }

    @Then("response status should be {int}")
    public void responseStatusIs(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("response should contain error message {string}")
    public void responseContainsErrorMessage(String message) {
        response.then().body("message", equalTo(message));
    }
}
