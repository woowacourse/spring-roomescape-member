package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.theme.WeeklyThemeResponse;
import roomescape.dto.time.TimeRequest;
import roomescape.service.reservation.ReservationService;
import roomescape.service.reservation.ReservationThemeService;
import roomescape.service.reservation.ReservationTimeService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
class ReservationThemeServiceTest {

    @Autowired
    private ReservationThemeService themeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService timeService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void getAllThemes() {
        // given
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));

        // when
        List<ThemeResponse> themes = themeService.getAllThemes();

        // then
        assertThat(themes).hasSize(1);
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void insertTheme() {
        // given
        ThemeResponse response = themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));

        // when
        Long insertedThemeId = response.id();

        // then
        assertThat(insertedThemeId).isEqualTo(1L);
    }

    @DisplayName("동일한 이름의 테마는 추가할 수 없다.")
    @Test
    void insertSameNameTheme() {
        // given
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));

        // when
        ThemeRequest request = new ThemeRequest("name", "desc1", "thumb1");

        // then
        assertThatThrownBy(() -> themeService.insertTheme(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름이 동일한 테마가 존재합니다.");
    }

    @DisplayName("ID로 테마를 삭제한다.")
    @Test
    void deleteTheme() {
        // given
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));

        // when
        themeService.deleteTheme(1L);

        // then
        assertThat(reservationService.getAllReservations()).hasSize(0);
    }

    @DisplayName("예약이 존재하는 테마는 삭제할 수 없다.")
    @Test
    void deleteReservedTheme() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.now().plusHours(1)));
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationService.insertReservation(new ReservationRequest("user", LocalDate.now(), 1L, 1L));

        // when & then
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("예약이 존재하는 테마는 삭제할 수 없습니다.");
    }

    @DisplayName("일주일간 가장 많이 예약된 테마를 조회한다.")
    @Test
    @Sql(scripts = {"/test_schema.sql", "/test_weekly_theme.sql"})
    void getWeeklyBestThemes() {
        // given
        List<WeeklyThemeResponse> weeklyBestThemes = themeService.getWeeklyBestThemes();

        // when & then
        assertThat(weeklyBestThemes).hasSize(4);
        assertThat(weeklyBestThemes).extracting("name").containsExactly("test3", "test2", "test1", "test4");
    }
}
