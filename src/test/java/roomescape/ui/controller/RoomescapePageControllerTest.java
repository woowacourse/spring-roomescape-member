package roomescape.ui.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import roomescape.availability.service.AvailabilityService;
import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.holiday.domain.Holiday;
import roomescape.holiday.exception.HolidayException;
import roomescape.holiday.service.HolidayService;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.reservation.exception.ReservationException;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeException;
import roomescape.theme.service.ThemeService;
import roomescape.time.exception.TimeException;
import roomescape.time.service.TimeService;

@WebMvcTest(RoomescapePageController.class)
class RoomescapePageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private ThemeService themeService;

    @MockitoBean
    private TimeService timeService;

    @MockitoBean
    private HolidayService holidayService;

    @MockitoBean
    private AvailabilityService availabilityService;

    private void stubDashboardData() {
        Theme theme = new Theme("미궁의 유산", "고대 미궁", "https://example.com/theme.png").withId(1L);
        Mockito.when(reservationService.getAll()).thenReturn(List.of(
                new Reservation("브라운", LocalDate.of(2026, 5, 6), new ReservationTime(1L, "10:00", "11:00"), theme)
                        .withId(1L)
        ));
        Mockito.when(themeService.getAll()).thenReturn(List.of(
                theme
        ));
        Mockito.when(themeService.getBestThemes()).thenReturn(List.of(
                theme
        ));
        Mockito.when(timeService.findAll()).thenReturn(List.of(
                new ReservationTime(1L, "10:00", "11:00")
        ));
        Mockito.when(holidayService.getAll()).thenReturn(List.of(
                new Holiday(1L, LocalDate.of(2026, 5, 7))
        ));
        Mockito.when(availabilityService.getAvailableTimes(1L, LocalDate.of(2026, 5, 6))).thenReturn(List.of(
                new ReservationTime(1L, "10:00", "11:00")
        ));
    }

    @Test
    void dashboardPageRenders() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("방탈출 운영 화면")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("예약 관리")));
    }

    @Test
    void reservationsPageRendersReservationData() throws Exception {
        stubDashboardData();

        mockMvc.perform(get("/dashboard/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("브라운")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("미궁의 유산")));
    }

    @Test
    void availabilityPageRendersThemeOptionsAndResults() throws Exception {
        stubDashboardData();

        mockMvc.perform(get("/dashboard/availability")
                        .param("availableThemeId", "1")
                        .param("availableDate", "2026-05-06"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("테마 선택")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("미궁의 유산")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("10:00 (#1)")));
    }

    @Test
    void createReservation_redirectsWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/dashboard/reservations")
                        .param("name", "브라운")
                .param("date", "2026-05-06")
                .param("themeId", "1")
                .param("timeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/reservations"))
                .andExpect(flash().attribute("successMessage", "예약을 생성했습니다."));

        Mockito.verify(reservationService).create(Mockito.any(ReservationSaveServiceDto.class));
    }

    @Test
    void createReservation_redirectsWithSafeFailureMessage() throws Exception {
        Mockito.when(reservationService.create(Mockito.any(ReservationSaveServiceDto.class)))
                .thenThrow(new RoomescapeException(ErrorCode.DUPLICATE_RESERVATION));

        mockMvc.perform(post("/dashboard/reservations")
                        .param("name", "브라운")
                        .param("date", "2026-05-06")
                        .param("themeId", "1")
                        .param("timeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage", "이미 예약된 시간입니다."));
    }

    @Test
    void deleteTheme_redirectsWithSafeFailureMessage() throws Exception {
        Mockito.doThrow(new ThemeException(ErrorCode.THEME_NOT_FOUND))
                .when(themeService).deleteById(99L);

        mockMvc.perform(post("/dashboard/themes/99/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/themes"))
                .andExpect(flash().attribute("errorMessage", "삭제할 테마를 찾지 못했습니다."));
    }

    @Test
    void deleteTime_redirectsWithSafeFailureMessage() throws Exception {
        Mockito.doThrow(new TimeException(ErrorCode.TIME_NOT_FOUND))
                .when(timeService).deleteById(12L);

        mockMvc.perform(post("/dashboard/times/12/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage", "예약 시간이 존재하지 않습니다."));
    }

    @Test
    void cancelReservation_redirectsWithSafeFailureMessage() throws Exception {
        Mockito.doThrow(new ReservationException(ErrorCode.RESERVATION_NOT_FOUND))
                .when(reservationService).cancel(21L);

        mockMvc.perform(post("/dashboard/reservations/21/cancel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage", "취소할 예약을 찾지 못했습니다."));
    }

    @Test
    void deleteHoliday_redirectsWithSafeFailureMessage() throws Exception {
        Mockito.doThrow(new HolidayException(ErrorCode.HOLIDAY_NOT_FOUND))
                .when(holidayService).delete(7L);

        mockMvc.perform(post("/dashboard/holidays/7/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errorMessage", "삭제할 휴일을 찾지 못했습니다."));
    }

    @Test
    void createReservation_예상못한오류는_공통문구를_보여준다() throws Exception {
        Mockito.when(reservationService.create(Mockito.any(ReservationSaveServiceDto.class)))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(post("/dashboard/reservations")
                        .param("name", "브라운")
                        .param("date", "2026-05-06")
                        .param("themeId", "1")
                        .param("timeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/reservations"))
                .andExpect(flash().attribute("errorMessage", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}
