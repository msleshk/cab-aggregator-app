package cab.app.ratingservice.util;

import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.model.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    RatingResponse toDto(Rating rating);

    Rating toEntity(RatingRequest ratingRequest);
}
