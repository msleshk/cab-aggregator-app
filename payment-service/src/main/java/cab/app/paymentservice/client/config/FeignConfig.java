package cab.app.paymentservice.client.config;

import cab.app.paymentservice.client.decoder.CustomFeignClientDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder(){
        return new CustomFeignClientDecoder();
    }
}
