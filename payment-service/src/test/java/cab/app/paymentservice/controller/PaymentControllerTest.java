package cab.app.paymentservice.controller;

import cab.app.paymentservice.controller.impl.PaymentController;
import cab.app.paymentservice.dto.kafka.CreatePaymentRequest;
import cab.app.paymentservice.dto.request.PayRequest;
import cab.app.paymentservice.dto.response.PayResponse;
import cab.app.paymentservice.dto.response.PaymentResponse;
import cab.app.paymentservice.dto.response.ResponseList;
import cab.app.paymentservice.service.PaymentService;
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

import java.math.BigDecimal;
import java.util.Collections;

import static cab.app.paymentservice.utils.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreatePaymentRequest createPaymentRequest;
    private PayRequest payRequest;
    private PayResponse payResponse;
    private PaymentResponse paymentResponse;
    private ResponseList<PaymentResponse> responseList;

    @BeforeEach
    void setUp() {
        createPaymentRequest = CreatePaymentRequest.builder()
                .rideId(RIDE_ID)
                .passengerId(PASSENGER_ID)
                .driverId(DRIVER_ID)
                .cost(AMOUNT)
                .build();

        payRequest = PayRequest.builder()
                .rideId(RIDE_ID)
                .passengerId(PASSENGER_ID)
                .promoCode("DISCOUNT10")
                .build();

        payResponse = new PayResponse(PAYMENT_ID, "PAID");

        paymentResponse = new PaymentResponse(
                PAYMENT_ID, RIDE_ID, DRIVER_ID,
                PASSENGER_ID, "COMPLETED",
                AMOUNT, new BigDecimal("135.00")
        );

        responseList = new ResponseList<>(Collections.singletonList(paymentResponse));
    }

    @Test
    void shouldCreatePayment() throws Exception {
        doNothing().when(paymentService).createPayment(any(CreatePaymentRequest.class));

        mockMvc.perform(post(PAYMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPaymentRequest)))
                .andExpect(status().isCreated());

        verify(paymentService, times(1)).createPayment(any(CreatePaymentRequest.class));
    }

    @Test
    void shouldPayForRide() throws Exception {
        when(paymentService.payForRide(any(PayRequest.class))).thenReturn(payResponse);

        mockMvc.perform(patch(PAYMENT_URL + "/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(PAYMENT_ID))
                .andExpect(jsonPath("$.status").value("PAID"));

        verify(paymentService, times(1)).payForRide(any(PayRequest.class));
    }

    @Test
    void shouldDeletePayment() throws Exception {
        doNothing().when(paymentService).deletePayment(PAYMENT_ID);

        mockMvc.perform(delete(PAYMENT_URL + "/{paymentId}", PAYMENT_ID))
                .andExpect(status().isOk());

        verify(paymentService, times(1)).deletePayment(PAYMENT_ID);
    }

    @Test
    void shouldGetAllPayments() throws Exception {
        when(paymentService.getAllPayments(OFFSET, LIMIT)).thenReturn(responseList);

        mockMvc.perform(get(PAYMENT_URL)
                        .param("offset", String.valueOf(OFFSET))
                        .param("limit", String.valueOf(LIMIT))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(PAYMENT_ID))
                .andExpect(jsonPath("$.responseList[0].rideId").value(RIDE_ID))
                .andExpect(jsonPath("$.responseList[0].driverId").value(DRIVER_ID))
                .andExpect(jsonPath("$.responseList[0].passengerId").value(PASSENGER_ID))
                .andExpect(jsonPath("$.responseList[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$.responseList[0].cost").value(AMOUNT.setScale(1).toString()))
                .andExpect(jsonPath("$.responseList[0].costAfterPromoCode").value("135.0"));

        verify(paymentService, times(1)).getAllPayments(OFFSET, LIMIT);
    }

    @Test
    void shouldGetPaymentById() throws Exception {
        when(paymentService.getPaymentById(PAYMENT_ID)).thenReturn(paymentResponse);

        mockMvc.perform(get(PAYMENT_URL + "/{paymentId}", PAYMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PAYMENT_ID))
                .andExpect(jsonPath("$.rideId").value(RIDE_ID))
                .andExpect(jsonPath("$.driverId").value(DRIVER_ID))
                .andExpect(jsonPath("$.passengerId").value(PASSENGER_ID))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.cost").value(AMOUNT.setScale(1).toString()))
                .andExpect(jsonPath("$.costAfterPromoCode").value("135.0"));

        verify(paymentService, times(1)).getPaymentById(PAYMENT_ID);
    }

    @Test
    void shouldGetPaymentByRideId() throws Exception {
        when(paymentService.getPaymentByRide(RIDE_ID)).thenReturn(paymentResponse);

        mockMvc.perform(get(PAYMENT_URL + "/ride/{rideId}", RIDE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PAYMENT_ID))
                .andExpect(jsonPath("$.rideId").value(RIDE_ID))
                .andExpect(jsonPath("$.driverId").value(DRIVER_ID))
                .andExpect(jsonPath("$.passengerId").value(PASSENGER_ID))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.cost").value(AMOUNT.setScale(1).toString()))
                .andExpect(jsonPath("$.costAfterPromoCode").value("135.0"));

        verify(paymentService, times(1)).getPaymentByRide(RIDE_ID);
    }
}
