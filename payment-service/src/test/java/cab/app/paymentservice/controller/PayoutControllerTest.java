package cab.app.paymentservice.controller;

import cab.app.paymentservice.controller.impl.PayoutController;
import cab.app.paymentservice.dto.request.PayoutRequest;
import cab.app.paymentservice.dto.response.PayoutResponse;
import cab.app.paymentservice.service.PayoutService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static cab.app.paymentservice.utils.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PayoutController.class)
@ExtendWith(MockitoExtension.class)
class PayoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayoutService payoutService;

    @Autowired
    private ObjectMapper objectMapper;

    private PayoutRequest payoutRequest;
    private PayoutResponse payoutResponse;

    @BeforeEach
    void setUp() {
        payoutRequest = PayoutRequest.builder()
                .driverId(DRIVER_ID)
                .amount(BALANCE_AMOUNT)
                .build();

        payoutResponse = new PayoutResponse(PAYMENT_ID, BALANCE_AMOUNT);
    }

    @Test
    void shouldCreatePayout() throws Exception {
        when(payoutService.createPayout(any(PayoutRequest.class))).thenReturn(payoutResponse);

        mockMvc.perform(post(PAYOUT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payoutRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payoutId").value(PAYMENT_ID))
                .andExpect(jsonPath("$.amount").value(BALANCE_AMOUNT.setScale(1)));

        verify(payoutService, times(1)).createPayout(any(PayoutRequest.class));
    }
}
