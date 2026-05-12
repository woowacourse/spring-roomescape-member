package roomescape.time;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.dto.ReservationTimesResponse;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약시간_조회() throws Exception {
        given(reservationTimeService.read())
                .willReturn(ReservationTimesResponse.from(List.of(
                        new ReservationTimeResponse(1L, LocalTime.of(10, 00)),
                        new ReservationTimeResponse(2L, LocalTime.of(11, 00)),
                        new ReservationTimeResponse(3L, LocalTime.of(12, 00)),
                        new ReservationTimeResponse(4L, LocalTime.of(13, 00)),
                        new ReservationTimeResponse(5L, LocalTime.of(14, 00))
                )));

        mockMvc.perform(get("/api/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationTimes.size()").value(5));
    }

    @Test
    void 예약_가능한_시간_조회() throws Exception {
        given(reservationTimeService.readAvailableTimes(1L, LocalDate.of(2026, 05, 10)))
                .willReturn(ReservationTimesResponse.from(List.of(
                        new ReservationTimeResponse(1L, LocalTime.of(10, 00)),
                        new ReservationTimeResponse(2L, LocalTime.of(11, 00))
                )));

        mockMvc.perform(get("/api/times/available")
                        .param("theme_id", "1")
                        .param("date", "2026-05-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationTimes.size()").value(2));
    }
}
