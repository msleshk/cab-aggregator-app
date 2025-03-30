package cab.app.rideservice.client.config;

import cab.app.rideservice.client.decoder.CustomFeignClientDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignClientDecoder();
    }
}
