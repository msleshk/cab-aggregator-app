package cab.app.paymentservice.e2e;

import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

public class CucumberSteps {
    private static final String BASE = "http://localhost:8085/api/v1";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;

    @When("get driver balance by id {long}")
    public void getDriverBalanceById(Long driverId) {
        response = given()
                .pathParam("id", driverId)
                .when()
                .get(BASE + "/driver-balance/{id}");
    }

    @When("get passenger balance by id {long}")
    public void getPassengerBalanceById(Long passengerId) {
        response = given()
                .pathParam("id", passengerId)
                .when()
                .get(BASE + "/passenger-balance/{id}");
    }

    @When("get payment by id {long}")
    public void getPaymentById(Long paymentId) {
        response = given()
                .pathParam("id", paymentId)
                .when()
                .get(BASE + "/payment/{id}");
    }

    @When("get payment by ride id {long}")
    public void getPaymentByRideId(Long rideId) {
        response = given()
                .pathParam("id", rideId)
                .when()
                .get(BASE + "/payment/ride/{id}");
    }

    @When("get all payments with offset {int} and limit {int}")
    public void getAllPaymentsWithOffsetAndLimit(int offset, int limit) {
        response = given()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(BASE + "/payment");
    }

    @Then("response status is {int}")
    public void responseStatusIs(int status) {
        response.then().statusCode(status);
    }

    @When("create a new payment for ride {long} with passenger {long}, driver {long} and cost {double}")
    public void createNewPayment(Long rideId, Long passengerId, Long driverId, Double cost) throws Exception {
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .rideId(rideId)
                .passengerId(passengerId)
                .driverId(driverId)
                .cost(BigDecimal.valueOf(cost))
                .build();

        response = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post(BASE + "/payment");
    }

    @When("pay for ride {long} with passenger {long} and promo code {string}")
    public void payForRide(Long rideId, Long passengerId, String promoCode) throws Exception {
        PayRequest request = PayRequest.builder()
                .rideId(rideId)
                .passengerId(passengerId)
                .promoCode(promoCode.isEmpty() ? null : promoCode)
                .build();

        response = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .patch(BASE + "/payment/pay");
    }

    @When("delete payment by id {long}")
    public void deletePaymentById(Long paymentId) {
        response = given()
                .pathParam("paymentId", paymentId)
                .when()
                .delete(BASE + "/payment/{paymentId}");
    }

}
