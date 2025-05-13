package cab.app.ratingservice.dto.response.ride;

public record RideResponse(
        Long id,
        Long driverId,
        Long passengerId,
        String status
) {
}
