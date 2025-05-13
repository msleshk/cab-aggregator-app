package cab.app.rideservice.dto.response;

import lombok.*;

import java.util.List;

@Builder
public record ResponseList<T>(
        List<T> responseList
) {
}
