package cab.app.authservice.client.passenger;

import cab.app.authservice.dto.request.SignUpDto;
import cab.app.authservice.dto.response.PassengerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerClientContainer {

    private final PassengerClient passengerClient;

    @CircuitBreaker(name = "passenger-service-circuit-breaker")
    public PassengerResponse createPassenger(SignUpDto dto, String authToken) {
        return passengerClient.createPassenger(dto, authToken);
    }
}
