package cab.app.paymentservice.kafka;

import cab.app.paymentservice.dto.kafka.BalanceRequest;
import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.service.DriverBalanceService;
import cab.app.paymentservice.service.PassengerBalanceService;
import cab.app.paymentservice.service.PaymentService;
import org.slf4j.MDC;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static cab.app.paymentservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PaymentService paymentService;
    private final DriverBalanceService driverBalanceService;
    private final PassengerBalanceService passengerBalanceService;

    @KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createPayment(@Payload CreatePaymentRequest paymentRequest,
                              @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            paymentService.createPayment(paymentRequest);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = PASSENGER_BALANCE_TOPIC, groupId = PASSENGER_BALANCE_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createPassengerBalance(@Payload BalanceRequest passengerBalanceRequest,
                                       @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            passengerBalanceService.createPassengerBalance(passengerBalanceRequest.id());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = DRIVER_BALANCE_TOPIC, groupId = DRIVER_BALANCE_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createDriverBalance(@Payload BalanceRequest driverBalanceRequest,
                                    @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            driverBalanceService.createDriverBalance(driverBalanceRequest.id());
        } finally {
            MDC.clear();
        }
    }


}
