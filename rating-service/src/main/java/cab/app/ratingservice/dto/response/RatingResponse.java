package cab.app.ratingservice.dto.response;


public record RatingResponse(
        String id,
        Long rideId,
        Long userId,
        String userRole,
        Integer rating,
        String comment
) {
}
