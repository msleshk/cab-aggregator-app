package cab.app.ratingservice.client.ride;

import cab.app.ratingservice.dto.response.ride.RideResponse;
import cab.app.ratingservice.exception.ExternalServerErrorException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RideClientContainer {

    private final RideClient rideClient;

    @CircuitBreaker(name = "rideClient", fallbackMethod = "fallback")
    public RideResponse getRideById(Long id) {
        return rideClient.getRideById(id);
    }

    private RideResponse fallback(Long id, Throwable ex) {
        log.warn("Fallback triggered in RideClient for ID: {}, error: {}", id, ex.getMessage());
        throw new ExternalServerErrorException(ex.getMessage());
    }
}


