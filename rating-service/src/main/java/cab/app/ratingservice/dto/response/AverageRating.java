package cab.app.ratingservice.dto.response;

import lombok.Builder;

@Builder
public record AverageRating(
        Long id,
        Double avgRating
) {
}
