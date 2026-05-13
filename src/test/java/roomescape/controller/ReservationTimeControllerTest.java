package roomescape.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeResponses;
import roomescape.service.ReservationTimeService;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @Test
    void GET_times_목록과_hasNext를_응답한다() throws Exception {
        given(reservationTimeService.getReservationTimes(0, 20))
                .willReturn(ReservationTimeResponses.of(
                        List.of(new ReservationTime(1L, LocalTime.of(10, 0))), false));

        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.times.size()").value(1))
                .andExpect(jsonPath("$.times[0].startAt").value("10:00"))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    void GET_times_id_단건을_응답한다() throws Exception {
        given(reservationTimeService.getReservationTime(1L))
                .willReturn(new ReservationTime(1L, LocalTime.of(10, 0)));

        mockMvc.perform(get("/times/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startAt").value("10:00"));
    }
}