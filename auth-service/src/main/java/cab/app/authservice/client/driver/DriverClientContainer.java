package cab.app.authservice.client.driver;

import cab.app.authservice.dto.request.SignUpDto;
import cab.app.authservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverClientContainer {
    private final DriverClient driverClient;

    public DriverResponse createDriver(SignUpDto dto, String authToken) {
        return driverClient.createDriver(dto, authToken);
    }
}
