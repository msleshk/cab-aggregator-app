package cab.app.rideservice.dto.response;

import lombok.Builder;

@Builder
public record ExceptionDto(String message) {
}
