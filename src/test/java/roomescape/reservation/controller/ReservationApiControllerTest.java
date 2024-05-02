package roomescape.reservation.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.dto.TimeResponse;
import roomescape.reservation.service.ReservationService;

@WebMvcTest(ReservationApiController.class)
class ReservationApiControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 목록 조회에 성공하면 200 응답을 받는다.")
    void getReservationRequestTest() throws Exception {
        mockMvc.perform(get("/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약을 성공적으로 추가하면 201 응답과 Location 헤더에 리소스 저장 경로를 받는다.")
    void createReservationRequestTest() throws Exception {
        ReservationRequest reservationRequest = new ReservationRequest("hogi", LocalDate.now(), 1L, 1L);
        ThemeResponse themeResponse = new ThemeResponse(1L, "공포", "무서운 테마", "https://i.pinimg.com/236x.jpg");
        TimeResponse timeResponse = new TimeResponse(1L, LocalTime.now());
        ReservationResponse reservationResponse = new ReservationResponse(1L, "hogi", reservationRequest.date(),
                themeResponse, timeResponse);

        doReturn(1L).when(reservationService)
                .save(reservationRequest);

        doReturn(reservationResponse).when(reservationService)
                .findById(1L);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/1"))
                .andExpect(jsonPath("$.id").value(reservationResponse.id()));
    }

    @Test
    @DisplayName("예약을 성공적으로 제거하면 204 응답을 받는다.")
    void deleteReservationRequestTest() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
