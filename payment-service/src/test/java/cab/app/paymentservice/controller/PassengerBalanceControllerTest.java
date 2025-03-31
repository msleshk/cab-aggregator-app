package cab.app.paymentservice.controller;

import cab.app.paymentservice.controller.impl.PassengerBalanceController;
import cab.app.paymentservice.dto.request.DepositRequest;
import cab.app.paymentservice.dto.response.BalanceResponse;
import cab.app.paymentservice.service.PassengerBalanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static cab.app.paymentservice.utils.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PassengerBalanceController.class)
@ExtendWith(MockitoExtension.class)
class PassengerBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerBalanceService passengerBalanceService;

    @Autowired
    private ObjectMapper objectMapper;

    private BalanceResponse balanceResponse;
    private DepositRequest depositRequest;

    @BeforeEach
    void setUp() {
        balanceResponse = new BalanceResponse(PASSENGER_ID, BALANCE_AMOUNT);
        depositRequest = new DepositRequest(DEPOSIT_AMOUNT);
    }

    @Test
    void shouldReturnPassengerBalance() throws Exception {
        when(passengerBalanceService.getPassengerBalance(PASSENGER_ID))
                .thenReturn(balanceResponse);

        mockMvc.perform(get(PASSENGER_BALANCE_URL + "/{id}", PASSENGER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(BALANCE_AMOUNT.setScale(1)));

        verify(passengerBalanceService, times(1)).getPassengerBalance(PASSENGER_ID);
    }

    @Test
    void shouldDepositToPassengerBalance() throws Exception {
        doNothing().when(passengerBalanceService).increasePassengerBalance(
                PASSENGER_ID, DEPOSIT_AMOUNT);

        mockMvc.perform(patch(PASSENGER_BALANCE_URL + "/{id}", PASSENGER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk());

        verify(passengerBalanceService, times(1))
                .increasePassengerBalance(PASSENGER_ID, DEPOSIT_AMOUNT);
    }
}

