package com.example.driverservice.controller;

import com.example.driverservice.controller.impl.CarController;
import com.example.driverservice.dto.request.CarRequest;
import com.example.driverservice.dto.response.CarResponse;
import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.service.CarService;
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

import static com.example.driverservice.utils.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarController.class)
@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private CarRequest carRequest;
    private CarResponse carResponse;
    private ResponseList<CarResponse> carResponseList;

    @BeforeEach
    void setUp() {
        carRequest = new CarRequest(
                BRAND,
                MODEL,
                COLOR,
                CAR_NUMBER
        );

        carResponse = new CarResponse(
                CAR_ID,
                BRAND,
                MODEL,
                COLOR,
                CAR_NUMBER,
                DRIVER_ID
        );

        carResponseList = new ResponseList<>(List.of(carResponse));
    }

    @Test
    void shouldAddCar() throws Exception {
        doNothing().when(carService).addCar(any(CarRequest.class));

        mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isCreated());

        verify(carService, times(1)).addCar(any(CarRequest.class));
    }

    @Test
    void shouldDeleteCar() throws Exception {
        doNothing().when(carService).deleteCar(CAR_ID);

        mockMvc.perform(delete(CAR_ID_URL, CAR_ID))
                .andExpect(status().isOk());

        verify(carService, times(1)).deleteCar(CAR_ID);
    }

    @Test
    void shouldUpdateCar() throws Exception {
        doNothing().when(carService).updateCar(eq(CAR_ID), any(CarRequest.class));

        mockMvc.perform(patch(CAR_ID_URL, CAR_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isOk());

        verify(carService, times(1)).updateCar(eq(CAR_ID), any(CarRequest.class));
    }

    @Test
    void shouldGetCarById() throws Exception {
        when(carService.getCarById(CAR_ID)).thenReturn(carResponse);

        mockMvc.perform(get(CAR_ID_URL, CAR_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CAR_ID))
                .andExpect(jsonPath("$.brand").value(BRAND))
                .andExpect(jsonPath("$.model").value(MODEL))
                .andExpect(jsonPath("$.color").value(COLOR))
                .andExpect(jsonPath("$.carNumber").value(CAR_NUMBER))
                .andExpect(jsonPath("$.driverId").value(DRIVER_ID));

        verify(carService, times(1)).getCarById(CAR_ID);
    }

    @Test
    void shouldGetAllCars() throws Exception {
        when(carService.getAllCars(0, 10)).thenReturn(carResponseList);

        mockMvc.perform(get(CAR_BASE_URL)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(CAR_ID))
                .andExpect(jsonPath("$.responseList[0].brand").value(BRAND))
                .andExpect(jsonPath("$.responseList[0].model").value(MODEL))
                .andExpect(jsonPath("$.responseList[0].color").value(COLOR))
                .andExpect(jsonPath("$.responseList[0].carNumber").value(CAR_NUMBER))
                .andExpect(jsonPath("$.responseList[0].driverId").value(DRIVER_ID));

        verify(carService, times(1)).getAllCars(0, 10);
    }
}
