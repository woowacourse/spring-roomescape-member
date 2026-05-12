package roomescape.presentation.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.presentation.PageController;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.application.ThemeService;
import roomescape.theme.domain.Theme;
import roomescape.time.application.ReservationTimeService;
import roomescape.time.domain.ReservationTime;

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

    @MockitoBean
    private Clock clock;

    @Test
    void reservationPageReturnsTemplateAndModel() throws Exception {
        given(themeService.getThemes()).willReturn(List.of(themeResponse()));
        given(themeService.getWeeksTopThemes(clock)).willReturn(List.of(themeResponse()));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("themes", "popularThemes", "today"))
                .andExpect(model().attribute("today", LocalDate.now()));
    }

    @Test
    void reservationAliasReturnsTemplateAndModel() throws Exception {
        given(themeService.getThemes()).willReturn(List.of(themeResponse()));
        given(themeService.getWeeksTopThemes(clock)).willReturn(List.of(themeResponse()));

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

    private Theme themeResponse() {
        return Theme.builder()
                .id(1L)
                .name("미드나잇")
                .thumbnailImageUrl("https://example.com/theme.png")
                .description("추리 테마")
                .durationTime(LocalTime.of(1, 30))
                .build();
    }

    private ReservationTime timeResponse() {
        return ReservationTime.builder()
                .id(1L)
                .startAt(LocalTime.of(10, 0))
                .build();
    }

    private Reservation reservationResponse() {
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

        return Reservation.builder()
                .id(1L)
                .name("포비")
                .date(LocalDate.of(2026, 5, 4))
                .time(time)
                .theme(theme)
                .build();
    }
}
