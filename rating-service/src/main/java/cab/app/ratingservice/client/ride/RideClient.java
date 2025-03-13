package cab.app.ratingservice.client.ride;

import cab.app.ratingservice.client.config.FeignConfig;
import cab.app.ratingservice.dto.response.ride.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.client.name.ride}", url = "${feign.client.path.ride}", configuration = FeignConfig.class)
public interface RideClient {
    @GetMapping("/{id}")
    RideResponse getRideById(@PathVariable("id") Long rideId);
}
