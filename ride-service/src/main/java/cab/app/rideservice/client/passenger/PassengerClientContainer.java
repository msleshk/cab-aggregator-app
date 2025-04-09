package cab.app.rideservice.client.passenger;

import cab.app.rideservice.exception.ExternalServerErrorException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PassengerClientContainer {

    private final PassengerClient passengerClient;

    @CircuitBreaker(name = "passengerClient", fallbackMethod = "fallback")
    public void getPassengerById(Long id) {
        passengerClient.getPassengerById(id);
    }

    private void fallback(Long id, Throwable ex) {
        log.warn("Fallback triggered in PassengerClient for ID: {}, error: {}", id, ex.getMessage());
        throw new ExternalServerErrorException(ex.getMessage());
    }
}