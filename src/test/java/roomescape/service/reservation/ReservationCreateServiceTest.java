package roomescape.service.reservation;

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
import roomescape.service.reservationtime.ReservationTimeCreateService;
import roomescape.service.theme.ThemeCreateService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class ReservationCreateServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationCreateService reservationCreateService;
    private ReservationTimeCreateService reservationTimeCreateService;
    private ThemeCreateService themeCreateService;

    @Autowired
    public ReservationCreateServiceTest(JdbcTemplate jdbcTemplate) {
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
    }

    @Test
    @DisplayName("예약 가능한 시간인 경우 성공한다.")
    void checkDuplicateReservationTime_Success() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(
                new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));
        Theme theme = themeCreateService.createTheme(
                new SaveThemeRequest("capy", "caoyDescription", "caoyThumbnail"));
        SaveReservationRequest request = new SaveReservationRequest(
                "capy", LocalDate.now().plusDays(1L), reservationTime.getId(), theme.getId());

        assertThatCode(() -> reservationCreateService.createReservation(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약된 시간인 경우 예외가 발생한다.")
    void checkDuplicateReservationTime_Failure() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(
                new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));
        Theme theme = themeCreateService.createTheme(
                new SaveThemeRequest("capy", "caoyDescription", "caoyThumbnail"));
        SaveReservationRequest request = new SaveReservationRequest(
                "capy", LocalDate.now().plusDays(1L), reservationTime.getId(), theme.getId());
        reservationCreateService.createReservation(request);

        assertThatThrownBy(() -> reservationCreateService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간에 이미 예약된 테마입니다.");
    }

    @Test
    @DisplayName("지나간 날짜와 시간에 대한 예약 생성시 예외가 발생한다.")
    void checkReservationDateTimeIsFuture_Failure() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(
                new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));
        Theme theme = themeCreateService.createTheme(
                new SaveThemeRequest("capy", "caoyDescription", "caoyThumbnail"));
        SaveReservationRequest request = new SaveReservationRequest(
                "capy", LocalDate.now().minusDays(1L), reservationTime.getId(), theme.getId());

        assertThatThrownBy(() -> reservationCreateService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }
}
