package cab.app.paymentservice.kafka;

import cab.app.paymentservice.dto.kafka.BalanceRequest;
import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.service.DriverBalanceService;
import cab.app.paymentservice.service.PassengerBalanceService;
import cab.app.paymentservice.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static cab.app.paymentservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PaymentService paymentService;
    private final DriverBalanceService driverBalanceService;
    private final PassengerBalanceService passengerBalanceService;

    @KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createPayment(CreatePaymentRequest paymentRequest) {
        paymentService.createPayment(paymentRequest);
    }

    @KafkaListener(topics = PASSENGER_BALANCE_TOPIC, groupId = PASSENGER_BALANCE_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createPassengerBalance(BalanceRequest passengerBalanceRequest) {
        passengerBalanceService.createPassengerBalance(passengerBalanceRequest.id());
    }

    @KafkaListener(topics = DRIVER_BALANCE_TOPIC, groupId = DRIVER_BALANCE_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createDriverBalance(BalanceRequest driverBalanceRequest) {
        driverBalanceService.createDriverBalance(driverBalanceRequest.id());
    }

}
