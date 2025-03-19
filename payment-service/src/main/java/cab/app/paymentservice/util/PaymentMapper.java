package cab.app.paymentservice.util;

import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(CreatePaymentRequest paymentRequest);

    @Mapping(source = "finalCost", target = "costAfterPromoCode")
    @Mapping(source = "status", target = "status")
    PaymentResponse toDto(Payment payment);
}
