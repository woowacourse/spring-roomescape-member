package roomescape.service.reservationtime;

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
import roomescape.service.theme.ThemeCreateService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class ReservationTimeDeleteServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationTimeDeleteService reservationTimeDeleteService;
    private ReservationTimeCreateService reservationTimeCreateService;
    private ReservationCreateService reservationCreateService;
    private ThemeCreateService themeCreateService;

    @Autowired
    public ReservationTimeDeleteServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        reservationTimeDeleteService = new ReservationTimeDeleteService(
                new ReservationTimeRepository(jdbcTemplate),
                new ReservationRepository(jdbcTemplate)
        );
        reservationTimeCreateService = new ReservationTimeCreateService(
                new ReservationTimeRepository(jdbcTemplate)
        );
        reservationCreateService = new ReservationCreateService(
                new ReservationRepository(jdbcTemplate),
                new ReservationTimeRepository(jdbcTemplate),
                new ThemeRepository(jdbcTemplate)
        );
        themeCreateService = new ThemeCreateService(new ThemeRepository(jdbcTemplate));
    }

    @Test
    @DisplayName("예약중이 아닌 시간을 삭제할 시 성공한다.")
    void deleteNotReservedTime_Success() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));

        assertThatCode(() -> reservationTimeDeleteService.deleteReservationTime(reservationTime.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 예약중인 시간을 삭제할 시 예외가 발생한다.")
    void deleteReservedTime_Failure() {
        ReservationTime reservationTime = reservationTimeCreateService.createReservationTime(new SaveReservationTimeRequest(LocalTime.now().plusHours(1L)));
        Theme theme = themeCreateService.createTheme(
                new SaveThemeRequest("capy", "caoyDescription", "caoyThumbnail"));
        SaveReservationRequest request = new SaveReservationRequest("capy", LocalDate.now().plusDays(1L), reservationTime.getId(), theme.getId());
        reservationCreateService.createReservation(request);

        assertThatThrownBy(() -> reservationTimeDeleteService.deleteReservationTime(reservationTime.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약중인 시간은 삭제할 수 없습니다.");
    }
}
