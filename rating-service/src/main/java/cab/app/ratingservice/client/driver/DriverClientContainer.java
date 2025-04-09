package cab.app.ratingservice.client.driver;

import cab.app.ratingservice.exception.ExternalServerErrorException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverClientContainer {

    private final DriverClientContainer driverClient;

    @CircuitBreaker(name = "driverClient", fallbackMethod = "fallback")
    public void getDriverById(Long id) {
        driverClient.getDriverById(id);
    }

    private void fallback(Long id, Throwable ex) {
        log.warn("Driver service fallback triggered for ID: {}, error: {}", id, ex.getMessage());
        throw new ExternalServerErrorException(ex.getMessage());
    }
}
