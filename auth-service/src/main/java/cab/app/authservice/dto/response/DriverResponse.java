package cab.app.authservice.dto.response;

public record DriverResponse(
        String name,

        String email,

        String phoneNumber,

        String gender
) implements UserResponse {
}
