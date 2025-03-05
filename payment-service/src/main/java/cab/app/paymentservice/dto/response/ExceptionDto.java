package cab.app.paymentservice.dto.response;

import lombok.Builder;

@Builder
public record ExceptionDto(String message) {
}
