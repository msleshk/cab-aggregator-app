package cab.app.rideservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static cab.app.rideservice.kafka.util.Constants.DRIVER_STATUS_TOPIC;
import static cab.app.rideservice.kafka.util.Constants.PAYMENT_TOPIC;

@Configuration
public class KafkaTopic {

    private final int partitionCount = 3;

    @Bean
    public NewTopic newPaymentTopic() {
        return TopicBuilder.name(PAYMENT_TOPIC)
                .partitions(partitionCount)
                .build();
    }

    @Bean
    public NewTopic newDriverTopic(){
        return TopicBuilder.name(DRIVER_STATUS_TOPIC)
                .partitions(partitionCount)
                .build();
    }
}
