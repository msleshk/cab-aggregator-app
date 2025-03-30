package cab.app.rideservice.kafka;

import cab.app.rideservice.dto.kafka.CreatePayment;
import cab.app.rideservice.dto.kafka.UpdateDriverStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static cab.app.rideservice.kafka.util.Constants.DRIVER_STATUS_TOPIC;
import static cab.app.rideservice.kafka.util.Constants.PAYMENT_TOPIC;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNewPayment(CreatePayment payment) {
        kafkaTemplate.send(PAYMENT_TOPIC, payment);
        System.out.println("sending message");
    }

    public void sendDriverStatusUpdate(UpdateDriverStatus driverStatusUpdate){
        kafkaTemplate.send(DRIVER_STATUS_TOPIC, driverStatusUpdate);
    }
}
