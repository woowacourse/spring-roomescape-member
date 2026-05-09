package roomescape.reservationtime.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservationtime.domain.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.UserReservationTimeService;

@WebMvcTest(UserReservationTimeController.class)
class UserReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserReservationTimeService userReservationTimeService;

    @Test
    void 예약_시간들을_반환한다() throws Exception {
        List<ReservationTime> mockTimes = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(13, 0))
        );
        given(userReservationTimeService.getReservationTimes()).willReturn(mockTimes);

        mockMvc.perform(get("/times"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].startAt").value("10:00:00"))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void 예약_가능한_시간을_반환한다() throws Exception {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 8);

        List<AvailableTime> mockAvailableTimes = List.of(
                new AvailableTime(1L, LocalTime.of(10, 0), true),
                new AvailableTime(2L, LocalTime.of(11, 0), false)
        );

        given(userReservationTimeService.getSchedules(eq(date), eq(themeId)))
                .willReturn(mockAvailableTimes);

        mockMvc.perform(get("/times/{themeId}", themeId)
                        .param("date", "2026-05-08"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.themeId").value(1L))
                .andExpect(jsonPath("$.date").value("2026-05-08"))
                .andExpect(jsonPath("$.schedules.size()").value(2))
                .andExpect(jsonPath("$.schedules[0].timeId").value(1L))
                .andExpect(jsonPath("$.schedules[0].isAvailable").value(true))
                .andExpect(jsonPath("$.schedules[1].isAvailable").value(false));
    }
}