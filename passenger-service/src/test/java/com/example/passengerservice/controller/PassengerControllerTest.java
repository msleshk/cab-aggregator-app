package com.example.passengerservice.controller;

import com.example.passengerservice.controller.impl.PassengerController;
import com.example.passengerservice.dto.request.PassengerRequest;
import com.example.passengerservice.dto.response.PassengerResponse;
import com.example.passengerservice.dto.response.PassengerResponseList;
import com.example.passengerservice.service.PassengerService;
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

import java.util.List;

import static com.example.passengerservice.utils.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PassengerController.class)
@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerService passengerService;

    @Autowired
    private ObjectMapper objectMapper;

    private PassengerRequest passengerRequest;
    private PassengerResponse passengerResponse;
    private PassengerResponseList passengerResponseList;

    @BeforeEach
    void setUp() {
        passengerRequest = new PassengerRequest(PASSENGER_NAME, PASSENGER_EMAIL, PASSENGER_PHONE);

        passengerResponse = new PassengerResponse(PASSENGER_ID, PASSENGER_NAME, PASSENGER_EMAIL, PASSENGER_PHONE, AVERAGE_RATING);

        passengerResponseList = new PassengerResponseList(List.of(passengerResponse));
    }

    @Test
    void shouldAddPassenger() throws Exception {
        doNothing().when(passengerService).addPassenger(any(PassengerRequest.class));

        mockMvc.perform(post(PASSENGER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerRequest)))
                .andExpect(status().isCreated());

        verify(passengerService, times(1)).addPassenger(any(PassengerRequest.class));
    }

    @Test
    void shouldDeletePassenger() throws Exception {
        doNothing().when(passengerService).deletePassengerById(PASSENGER_ID);

        mockMvc.perform(delete(PASSENGER_ID_URL, PASSENGER_ID))
                .andExpect(status().isOk());

        verify(passengerService, times(1)).deletePassengerById(PASSENGER_ID);
    }

    @Test
    void shouldUpdatePassenger() throws Exception {
        doNothing().when(passengerService).updatePassenger(eq(PASSENGER_ID), any(PassengerRequest.class));

        mockMvc.perform(patch(PASSENGER_ID_URL, PASSENGER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passengerRequest)))
                .andExpect(status().isOk());

        verify(passengerService, times(1)).updatePassenger(eq(PASSENGER_ID), any(PassengerRequest.class));
    }

    @Test
    void shouldGetPassengerById() throws Exception {
        when(passengerService.getPassengerById(PASSENGER_ID)).thenReturn(passengerResponse);

        mockMvc.perform(get(PASSENGER_ID_URL, PASSENGER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PASSENGER_ID))
                .andExpect(jsonPath("$.name").value(PASSENGER_NAME))
                .andExpect(jsonPath("$.email").value(PASSENGER_EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(PASSENGER_PHONE))
                .andExpect(jsonPath("$.averageRating").value(AVERAGE_RATING));

        verify(passengerService, times(1)).getPassengerById(PASSENGER_ID);
    }

    @Test
    void shouldGetPassengers() throws Exception {
        when(passengerService.getAllPassengers(0, 10)).thenReturn(passengerResponseList);

        mockMvc.perform(get(PASSENGER_BASE_URL)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(PASSENGER_ID))
                .andExpect(jsonPath("$.responseList[0].name").value(PASSENGER_NAME))
                .andExpect(jsonPath("$.responseList[0].email").value(PASSENGER_EMAIL))
                .andExpect(jsonPath("$.responseList[0].phoneNumber").value(PASSENGER_PHONE))
                .andExpect(jsonPath("$.responseList[0].averageRating").value(AVERAGE_RATING));

        verify(passengerService, times(1)).getAllPassengers(0, 10);
    }

}
