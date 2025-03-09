package cab.app.paymentservice.client.ride;

import cab.app.paymentservice.dto.response.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.client.name.ride}", url = "${feign.client.path.ride}")
public interface RideClient {
    @GetMapping("/{id}")
    RideResponse getRideById(@PathVariable("id") Long rideId);
}
