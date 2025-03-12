package cab.app.paymentservice.service.implementation;

import cab.app.paymentservice.client.driver.DriverClient;
import cab.app.paymentservice.exception.EntityNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityValidator {
    private final DriverClient driverClient;

    public void checkIfDriverExist(Long driverId) {
        try {
            driverClient.getDriverById(driverId);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }
    }
}
