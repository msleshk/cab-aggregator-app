package com.example.driverservice.controller;

import com.example.driverservice.controller.impl.DriverController;
import com.example.driverservice.dto.request.DriverRequest;
import com.example.driverservice.dto.response.DriverResponse;
import com.example.driverservice.dto.response.ResponseList;
import com.example.driverservice.model.enums.DriverStatus;
import com.example.driverservice.model.enums.Gender;
import com.example.driverservice.service.DriverService;
import com.example.driverservice.utils.TestConstants;
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

import static com.example.driverservice.utils.TestConstants.DRIVER_BASE_URL;
import static com.example.driverservice.utils.TestConstants.DRIVER_ID_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DriverController.class)
@ExtendWith(MockitoExtension.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Autowired
    private ObjectMapper objectMapper;

    private DriverRequest driverRequest;
    private DriverResponse driverResponse;
    private ResponseList<DriverResponse> driverResponseList;

    @BeforeEach
    void setUp() {
        driverRequest = new DriverRequest(
                TestConstants.DRIVER_NAME,
                TestConstants.DRIVER_EMAIL,
                TestConstants.DRIVER_PHONE,
                Gender.MALE,
                TestConstants.CAR_ID
        );

        driverResponse = new DriverResponse(
                TestConstants.DRIVER_ID,
                TestConstants.DRIVER_NAME,
                TestConstants.DRIVER_EMAIL,
                TestConstants.AVERAGE_RATING,
                TestConstants.DRIVER_PHONE,
                Gender.MALE,
                DriverStatus.AVAILABLE,
                TestConstants.CAR_ID
        );

        driverResponseList = new ResponseList<>(List.of(driverResponse));
    }

    @Test
    void shouldAddDriver() throws Exception {
        doNothing().when(driverService).addDriver(any(DriverRequest.class));

        mockMvc.perform(post(DRIVER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isCreated());

        verify(driverService, times(1)).addDriver(any(DriverRequest.class));
    }

    @Test
    void shouldDeleteDriver() throws Exception {
        doNothing().when(driverService).deleteDriver(TestConstants.DRIVER_ID);

        mockMvc.perform(delete(DRIVER_ID_URL, TestConstants.DRIVER_ID))
                .andExpect(status().isOk());

        verify(driverService, times(1)).deleteDriver(TestConstants.DRIVER_ID);
    }

    @Test
    void shouldUpdateDriver() throws Exception {
        doNothing().when(driverService).updateDriver(eq(TestConstants.DRIVER_ID), any(DriverRequest.class));

        mockMvc.perform(patch(DRIVER_ID_URL, TestConstants.DRIVER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk());

        verify(driverService, times(1)).updateDriver(eq(TestConstants.DRIVER_ID), any(DriverRequest.class));
    }

    @Test
    void shouldGetDriverById() throws Exception {
        when(driverService.getDriverById(TestConstants.DRIVER_ID)).thenReturn(driverResponse);

        mockMvc.perform(get(DRIVER_ID_URL, TestConstants.DRIVER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TestConstants.DRIVER_ID))
                .andExpect(jsonPath("$.name").value(TestConstants.DRIVER_NAME))
                .andExpect(jsonPath("$.email").value(TestConstants.DRIVER_EMAIL))
                .andExpect(jsonPath("$.phoneNumber").value(TestConstants.DRIVER_PHONE))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.toString()))
                .andExpect(jsonPath("$.status").value(DriverStatus.AVAILABLE.toString()))
                .andExpect(jsonPath("$.carId").value(TestConstants.CAR_ID));

        verify(driverService, times(1)).getDriverById(TestConstants.DRIVER_ID);
    }

    @Test
    void shouldGetAllDrivers() throws Exception {
        when(driverService.getAllDrivers(0, 10)).thenReturn(driverResponseList);

        mockMvc.perform(get(DRIVER_BASE_URL)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(TestConstants.DRIVER_ID))
                .andExpect(jsonPath("$.responseList[0].name").value(TestConstants.DRIVER_NAME))
                .andExpect(jsonPath("$.responseList[0].email").value(TestConstants.DRIVER_EMAIL))
                .andExpect(jsonPath("$.responseList[0].phoneNumber").value(TestConstants.DRIVER_PHONE))
                .andExpect(jsonPath("$.responseList[0].gender").value(Gender.MALE.toString()))
                .andExpect(jsonPath("$.responseList[0].status").value(DriverStatus.AVAILABLE.toString()))
                .andExpect(jsonPath("$.responseList[0].carId").value(TestConstants.CAR_ID));

        verify(driverService, times(1)).getAllDrivers(0, 10);
    }
}

