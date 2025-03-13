package cab.app.rideservice.dto.response;

import lombok.Builder;

import java.util.HashMap;

@Builder
public record MultiException(String message, HashMap<String, String> errors) {
}
