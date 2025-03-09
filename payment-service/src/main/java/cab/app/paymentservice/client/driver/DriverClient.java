package cab.app.paymentservice.client.driver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${feign.client.name.driver}", url = "${feign.client.path.driver}")
public interface DriverClient {
    @GetMapping("/{id}")
    void getDriverById(@PathVariable("id") Long driverId);
}
