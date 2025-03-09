package cab.app.rideservice.client.passenger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.client.name.passenger}", url = "${feign.client.path.passenger}")
public interface PassengerClient {
    @GetMapping("/{id}")
    void getPassengerById(@PathVariable("id") Long driverId);
}
