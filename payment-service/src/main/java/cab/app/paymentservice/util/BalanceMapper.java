package cab.app.paymentservice.util;

import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.model.DriverBalance;
import cab.app.paymentservice.model.PassengerBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceMapper {
    @Mapping(source = "passengerId", target = "id")
    BalanceResponse toDto(PassengerBalance balance);

    @Mapping(source = "driverId", target = "id")
    BalanceResponse toDto(DriverBalance balance);
}
