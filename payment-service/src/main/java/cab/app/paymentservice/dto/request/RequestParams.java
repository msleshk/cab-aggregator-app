package cab.app.paymentservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RequestParams(
        @Min(value = 0, message = "Offset should be greater than 0!")
        int offset,

        @Min(value = 1, message = "Limit should be greater than 1")
        @Max(value = 100, message = "Limit should be less than 100")
        int limit
) {
}
