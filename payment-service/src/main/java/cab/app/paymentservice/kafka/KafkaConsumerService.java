package cab.app.paymentservice.kafka;

import cab.app.paymentservice.dto.request.CreatePaymentRequest;
import cab.app.paymentservice.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static cab.app.paymentservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PaymentService paymentService;

    @KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createPayment(CreatePaymentRequest paymentRequest){
        paymentService.createPayment(paymentRequest);
    }
}
