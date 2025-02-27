package cab.app.paymentservice.util;

import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import cab.app.paymentservice.model.Payout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayoutMapper {
    @Mapping(source = "id", target = "payoutId")
    PayoutResponse toDto(Payout payout);

    Payout toEntity(PayoutRequest payoutRequest);
}
