package cab.app.authservice.dto.response;

public record PassengerResponse(
        String name,

        String email,

        String phone
) implements UserResponse {
}
