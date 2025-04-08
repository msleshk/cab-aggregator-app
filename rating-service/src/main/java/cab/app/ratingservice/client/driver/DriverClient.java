package cab.app.ratingservice.client.driver;

import cab.app.ratingservice.client.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.client.name.driver}", path = "${feign.client.path.driver}", configuration = FeignConfig.class)
public interface DriverClient {
    @GetMapping("/{id}")
    void getDriverById(@PathVariable("id") Long driverId);
}
