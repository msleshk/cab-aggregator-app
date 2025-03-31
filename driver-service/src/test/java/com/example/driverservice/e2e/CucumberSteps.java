package com.example.driverservice.e2e;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CucumberSteps {
    private static final String BASE_URL = "http://localhost:8084/api/v1";
    private Response response;

    @When("send POST request to {string} with body:")
    public void sendPostRequestWithBody(String url, String requestBody) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(BASE_URL + url);
    }

    @When("send GET request to {string}")
    public void sendGetRequest(String url) {
        response = given()
                .when()
                .get(BASE_URL + url);
    }

    @When("send GET request to {string} with offset {int} and limit {int}")
    public void sendGetRequestWithParams(String url, int offset, int limit) {
        response = given()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(BASE_URL + url);
    }

    @When("send PATCH request to {string} with body:")
    public void sendPatchRequest(String url, String requestBody) {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .patch(BASE_URL + url);
    }

    @When("send DELETE request to {string}")
    public void sendDeleteRequest(String url) {
        response = given()
                .when()
                .delete(BASE_URL + url);
    }

    @Then("response status should be {int}")
    public void responseStatusShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("response should contain error message {string}")
    public void responseShouldContainErrorMessage(String message) {
        response.then().body("message", equalTo(message));
    }
}
