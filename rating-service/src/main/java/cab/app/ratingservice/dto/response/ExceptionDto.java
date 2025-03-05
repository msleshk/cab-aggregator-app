package cab.app.ratingservice.dto.response;

import lombok.Builder;

@Builder
public record ExceptionDto(String message) {
}
