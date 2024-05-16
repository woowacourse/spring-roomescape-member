package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.ReservationTime;
import roomescape.service.LoginService;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeRequest;
import roomescape.service.dto.ReservationTimeResponse;

@WebMvcTest(ReservationTimeApiController.class)
class ReservationTimeApiControllerTest {


    @MockBean
    private ReservationTimeService reservationTimeService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("/times GET 요청 시 모든 예약 시간 정보와 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_all_reservation_times_when_get_request() throws Exception {
        List<ReservationTimeResponse> responseDtos = List.of(
                new ReservationTimeResponse(new ReservationTime(1L, "09:08")),
                new ReservationTimeResponse(new ReservationTime(2L, "11:30"))
        );

        given(reservationTimeService.findAllReservationTimes()).willReturn(responseDtos);

        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @DisplayName("/times POST 요청 시 저장된 예약 시간과 201 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_reservation_time_when_post_request() throws Exception {
        ReservationTimeRequest requestDto = new ReservationTimeRequest("01:34");
        String requestBody = objectMapper.writeValueAsString(requestDto);

        ReservationTimeResponse responseDto = new ReservationTimeResponse(new ReservationTime(1L, "01:34"));

        given(reservationTimeService.createReservationTime(any())).willReturn(responseDto);

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.startAt", is("01:34")));
    }

    @DisplayName("/times/{id} DELETE 요청 시 204 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_when_delete_request() throws Exception {
        mockMvc.perform(delete("/times/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
