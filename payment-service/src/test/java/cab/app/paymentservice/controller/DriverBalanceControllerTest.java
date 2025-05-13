package cab.app.paymentservice.controller;

import cab.app.paymentservice.controller.impl.DriverBalanceController;
import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.service.DriverBalanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static cab.app.paymentservice.utils.TestConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DriverBalanceController.class)
class DriverBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverBalanceService driverBalanceService;

    @Test
    void shouldReturnDriverBalance() throws Exception {
        BalanceResponse response = new BalanceResponse(DRIVER_ID, BALANCE_AMOUNT);
        when(driverBalanceService.getDriverBalance(DRIVER_ID)).thenReturn(response);

        mockMvc.perform(get(DRIVER_BALANCE_URL + "/" + DRIVER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(BALANCE_AMOUNT.setScale(1)));
    }


    @Test
    void shouldReturnNotFoundIfBalanceDoesNotExist() throws Exception {
        when(driverBalanceService.getDriverBalance(DRIVER_ID))
                .thenThrow(new RuntimeException(EXCEPTION_BALANCE_NOT_FOUND));

        mockMvc.perform(get(DRIVER_BALANCE_URL + "/" + DRIVER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

}
