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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import static cab.app.paymentservice.kafka.util.Constants.*;

@Component
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final PaymentService paymentService;
    private final DriverBalanceService driverBalanceService;
    private final PassengerBalanceService passengerBalanceService;
    private final JwtDecoder jwtDecoder;

    @KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createPayment(@Payload CreatePaymentRequest paymentRequest,
                              @Header(value = "Authorization", required = false) String authHeader,
                              @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            validateToken(authHeader);

            paymentService.createPayment(paymentRequest);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = PASSENGER_BALANCE_TOPIC, groupId = PASSENGER_BALANCE_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createPassengerBalance(@Payload BalanceRequest passengerBalanceRequest,
                                       @Header(value = "Authorization", required = false) String authHeader,
                                       @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            validateToken(authHeader);

            passengerBalanceService.createPassengerBalance(passengerBalanceRequest.id());
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = DRIVER_BALANCE_TOPIC, groupId = DRIVER_BALANCE_GROUP, containerFactory = CONTAINER_FACTORY)
    public void createDriverBalance(@Payload BalanceRequest driverBalanceRequest,
                                    @Header(value = "Authorization", required = false) String authHeader,
                                    @Header(name = "traceId", required = false) String traceId) {
        MDC.put("traceId", traceId != null ? traceId : "N/A");
        try {
            validateToken(authHeader);

            driverBalanceService.createDriverBalance(driverBalanceRequest.id());
        } finally {
            MDC.clear();
        }
    }

    private void validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Jwt jwt = jwtDecoder.decode(token);

        if (!jwt.getClaimAsStringList("roles").contains("YOUR_EXPECTED_ROLE")) {
            throw new AccessDeniedException("Invalid role");
        }
    }
}
