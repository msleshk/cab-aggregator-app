package cab.app.paymentservice.dto.response.exception;

import lombok.Builder;

@Builder
public record ExceptionDto(String message) {
}
