package cab.app.rideservice.controller;

import cab.app.rideservice.controller.impl.RideController;
import cab.app.rideservice.dto.request.RideRequest;
import cab.app.rideservice.dto.request.RideToUpdate;
import cab.app.rideservice.dto.response.ResponseList;
import cab.app.rideservice.dto.response.RideResponse;
import cab.app.rideservice.service.RideService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static cab.app.rideservice.utils.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = RideController.class)
class RideControllerTest {

    @MockBean
    private RideService rideService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateRide() throws Exception {
        RideRequest rideRequest = new RideRequest(USER_ID, DEPARTURE_ADDRESS, ARRIVAL_ADDRESS, SHORT_TRIP_DISTANCE);
        doNothing().when(rideService).createRide(any(RideRequest.class));

        mockMvc.perform(post(CREATE_RIDE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rideRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateRide() throws Exception {
        RideToUpdate rideToUpdate = new RideToUpdate(DEPARTURE_ADDRESS, ARRIVAL_ADDRESS, LONG_TRIP_DISTANCE);
        doNothing().when(rideService).updateRide(any(Long.class), any(RideToUpdate.class));

        mockMvc.perform(patch(UPDATE_RIDE, RIDE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rideToUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSetDriver() throws Exception {
        doNothing().when(rideService).assignDriver(any(Long.class), any(Long.class));

        mockMvc.perform(patch(SET_DRIVER, RIDE_ID, DRIVER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateRideStatus() throws Exception {
        doNothing().when(rideService).updateRideStatus(any(Long.class), any(String.class));

        mockMvc.perform(patch(UPDATE_RIDE_STATUS, RIDE_ID, RIDE_STATUS_COMPLETED))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRide() throws Exception {
        doNothing().when(rideService).deleteRide(any(Long.class));

        mockMvc.perform(delete(DELETE_RIDE, RIDE_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRideById() throws Exception {
        RideResponse rideResponse = new RideResponse(RIDE_ID, DRIVER_ID, USER_ID, DEPARTURE_ADDRESS, ARRIVAL_ADDRESS, RIDE_STATUS_COMPLETED, TIMESTAMP, BASE_FARE);
        when(rideService.getRide(any(Long.class))).thenReturn(rideResponse);

        mockMvc.perform(get(GET_RIDE_BY_ID, RIDE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RIDE_ID));
    }

    @Test
    void shouldGetAllRides() throws Exception {
        ResponseList<RideResponse> responseList = new ResponseList<>(Collections.singletonList(new RideResponse(RIDE_ID, DRIVER_ID, USER_ID, DEPARTURE_ADDRESS, ARRIVAL_ADDRESS, RIDE_STATUS_COMPLETED, TIMESTAMP, BASE_FARE)));
        when(rideService.getAllRides(any(Integer.class), any(Integer.class))).thenReturn(responseList);

        mockMvc.perform(get(BASE_URL)
                        .param("offset", String.valueOf(OFFSET))
                        .param("limit", String.valueOf(LIMIT)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(RIDE_ID));
    }
}
