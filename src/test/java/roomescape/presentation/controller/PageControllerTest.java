package roomescape.presentation.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.theme.application.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.presentation.dto.ThemeResponse;
import roomescape.time.application.ReservationTimeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.presentation.dto.ReservationTimeResponse;

@WebMvcTest(PageController.class)
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @Test
    void reservationPageReturnsTemplateAndModel() throws Exception {
        given(themeService.getThemes()).willReturn(List.of(themeResponse()));
        given(themeService.getWeeksTopThemes()).willReturn(List.of(themeResponse()));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("themes", "popularThemes", "today"))
                .andExpect(model().attribute("today", LocalDate.now()));
    }

    @Test
    void reservationAliasReturnsTemplateAndModel() throws Exception {
        given(themeService.getThemes()).willReturn(List.of(themeResponse()));
        given(themeService.getWeeksTopThemes()).willReturn(List.of(themeResponse()));

        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("themes", "popularThemes", "today"));
    }

    @Test
    void adminPageReturnsTemplateAndModel() throws Exception {
        given(themeService.getThemes()).willReturn(List.of(themeResponse()));
        given(reservationTimeService.getReservationTimes()).willReturn(List.of(timeResponse()));
        given(reservationService.getReservations()).willReturn(List.of(reservationResponse()));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("themes", "times", "reservations"));
    }

    private ThemeResponse themeResponse() {
        return ThemeResponse.builder()
                .id(1L)
                .name("미드나잇")
                .thumbnailImageUrl("https://example.com/theme.png")
                .description("추리 테마")
                .durationTime(LocalTime.of(1, 30))
                .build();
    }

    private ReservationTimeResponse timeResponse() {
        return ReservationTimeResponse.builder()
                .id(1L)
                .startAt(LocalTime.of(10, 0))
                .build();
    }

    private ReservationResponse reservationResponse() {
        Theme theme = Theme.builder()
                .id(1L)
                .name("미드나잇")
                .thumbnailImageUrl("https://example.com/theme.png")
                .description("추리 테마")
                .durationTime(LocalTime.of(1, 30))
                .build();
        ReservationTime time = ReservationTime.builder()
                .id(1L)
                .startAt(LocalTime.of(10, 0))
                .build();

        return ReservationResponse.builder()
                .id(1L)
                .name("포비")
                .date(LocalDate.of(2026, 5, 4))
                .time(ReservationTimeResponse.from(time))
                .theme(ThemeResponse.from(theme))
                .build();
    }
}
