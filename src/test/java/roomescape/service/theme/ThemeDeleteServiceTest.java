package roomescape.service.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.SaveReservationRequest;
import roomescape.service.dto.SaveReservationTimeRequest;
import roomescape.service.dto.SaveThemeRequest;
import roomescape.service.reservation.ReservationCreateService;
import roomescape.service.reservationtime.ReservationTimeCreateService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class ThemeDeleteServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationCreateService reservationCreateService;
    private ReservationTimeCreateService reservationTimeCreateService;
    private ThemeCreateService themeCreateService;
    private ThemeDeleteService themeDeleteService;

    @Autowired
    public ThemeDeleteServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        reservationCreateService = new ReservationCreateService(
                new ReservationRepository(jdbcTemplate),
                new ReservationTimeRepository(jdbcTemplate),
                new ThemeRepository(jdbcTemplate)
        );
        reservationTimeCreateService = new ReservationTimeCreateService(
                new ReservationTimeRepository(jdbcTemplate)
        );
        themeCreateService = new ThemeCreateService(new ThemeRepository(jdbcTemplate));
        themeDeleteService = new ThemeDeleteService(
                new ThemeRepository(jdbcTemplate),
                new ReservationRepository(jdbcTemplate)
        );
    }

    @Test
    @DisplayName("예약중이 아닌 테마를 삭제할 시 성공한다.")
    void deleteNotReservedTime_Success() {
        Theme theme = themeCreateService.createTheme(new SaveThemeRequest("capy", "caoyDescription", "caoyThumbnail"));

        assertThatCode(() -> themeDeleteService.deleteTheme(theme.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약중인 테마를 삭제할 시 예외가 발생한다.")
    void deleteReservedTime_Failure() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));
        Theme theme = themeCreateService.createTheme(
                new SaveThemeRequest("capy", "caoyDescription", "caoyThumbnail"));
        SaveReservationRequest request = new SaveReservationRequest("capy", LocalDate.now().plusDays(1L), reservationTime.getId(), theme.getId());
        reservationCreateService.createReservation(request);

        assertThatThrownBy(() -> themeDeleteService.deleteTheme(theme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약중인 테마는 삭제할 수 없습니다.");
    }
}
