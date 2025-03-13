package cab.app.paymentservice.dto.response;

import java.util.List;

public record ResponseList<T>(
        List<T> responseList
) {
}
