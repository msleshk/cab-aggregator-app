package cab.app.ratingservice.controller;

import cab.app.ratingservice.controller.impl.RatingController;
import cab.app.ratingservice.dto.request.RatingRequest;
import cab.app.ratingservice.dto.request.RatingToUpdate;
import cab.app.ratingservice.dto.response.AverageRating;
import cab.app.ratingservice.dto.response.RatingResponse;
import cab.app.ratingservice.dto.response.ResponseList;
import cab.app.ratingservice.service.RatingService;
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

import java.util.Collections;

import static cab.app.ratingservice.utils.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = RatingController.class)
class RatingControllerTest {

    @MockBean
    private RatingService ratingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private RatingRequest ratingRequest;
    private RatingToUpdate ratingToUpdate;
    private RatingResponse ratingResponse;
    private ResponseList<RatingResponse> responseList;

    @BeforeEach
    void setUp() {
        ratingRequest = new RatingRequest(RIDE_ID, USER_ID, ROLE, SCORE, COMMENT);
        ratingToUpdate = new RatingToUpdate(UPDATED_SCORE, UPDATED_COMMENT);
        ratingResponse = new RatingResponse(RATING_ID, RIDE_ID, USER_ID, ROLE, SCORE, COMMENT);
        responseList = new ResponseList<>(Collections.singletonList(ratingResponse));
    }

    @Test
    void shouldCreateRating() throws Exception {
        doNothing().when(ratingService).addRating(any(RatingRequest.class));

        mockMvc.perform(post(CREATE_RATING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateRating() throws Exception {
        doNothing().when(ratingService).updateRating(any(String.class), any(RatingToUpdate.class));

        mockMvc.perform(patch(UPDATE_RATING, RATING_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingToUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRating() throws Exception {
        doNothing().when(ratingService).deleteRating(any(String.class));

        mockMvc.perform(delete(DELETE_RATING, RATING_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRatingById() throws Exception {
        when(ratingService.getRatingById(any(String.class))).thenReturn(ratingResponse);

        mockMvc.perform(get(GET_RATING_BY_ID, RATING_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(RATING_ID));
    }

    @Test
    void shouldGetAllRatings() throws Exception {
        when(ratingService.getAllRating(any(Integer.class), any(Integer.class))).thenReturn(responseList);

        mockMvc.perform(get(BASE_URL)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(RATING_ID));
    }

    @Test
    void shouldGetAverageRatingByUser() throws Exception {
        AverageRating averageRating = new AverageRating(USER_ID, AVERAGE_SCORE);
        when(ratingService.getAverageRating(any(Long.class), any(String.class))).thenReturn(averageRating);

        mockMvc.perform(get(GET_AVG_RATING, USER_ID, ROLE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgRating").value(AVERAGE_SCORE));
    }

    @Test
    void shouldGetRatingByRideId() throws Exception {
        when(ratingService.getRatingByRideId(any(Long.class), any(Integer.class), any(Integer.class))).thenReturn(responseList);

        mockMvc.perform(get(GET_RATING_BY_RIDE, RIDE_ID)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(RATING_ID));
    }

    @Test
    void shouldGetRatingByUserAndRole() throws Exception {
        when(ratingService.getRatingByUserIdAndRole(any(Long.class), any(String.class), any(Integer.class), any(Integer.class))).thenReturn(responseList);

        mockMvc.perform(get(GET_RATING_BY_USER_ROLE, USER_ID, ROLE)
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseList[0].id").value(RATING_ID));
    }
}



