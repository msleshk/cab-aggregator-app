package cab.app.rideservice.client.driver;

import cab.app.rideservice.client.config.FeignConfig;
import cab.app.rideservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.client.name.driver}", path = "${feign.client.path.driver}", configuration = FeignConfig.class)
public interface DriverClient {
    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable("id") Long driverId);
}
