package roomescape.time.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import roomescape.reservation.domain.ReservationTime;
import roomescape.theme.service.ThemeService;
import roomescape.time.service.TimeService;

@WebMvcTest(TimeController.class)
class TimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TimeService timeService;

    @MockitoBean
    private ThemeService themeService;

    @Test
    void getAvailableTimes() throws Exception {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 6);

        List<ReservationTime> times = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0)),
                new ReservationTime(2L, LocalTime.of(12, 0), LocalTime.of(14, 0))
        );
        Mockito.when(themeService.getAvailableTimes(themeId, date)).thenReturn(times);

        mockMvc.perform(get("/times")
                        .queryParam("themeId", String.valueOf(themeId))
                        .queryParam("date", "2026-05-06")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAvailableTimes_잘못된_날짜형식이면_400() throws Exception {
        mockMvc.perform(get("/times")
                        .queryParam("themeId", "1")
                        .queryParam("date", "2026/05/06")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
