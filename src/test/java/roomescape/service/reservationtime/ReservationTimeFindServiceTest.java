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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ReservationTimeFindServiceTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationCreateService reservationCreateService;
    private ReservationTimeCreateService reservationTimeCreateService;
    private ReservationTimeFindService reservationTimeFindService;
    private ThemeCreateService themeCreateService;

    @Autowired
    public ReservationTimeFindServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        reservationCreateService = new ReservationCreateService(
                new ReservationRepository(jdbcTemplate),
                new ReservationTimeRepository(jdbcTemplate),
                new ThemeRepository(jdbcTemplate)
        );
        reservationTimeCreateService = new ReservationTimeCreateService(
                new ReservationTimeRepository(jdbcTemplate)
        );
        reservationTimeFindService = new ReservationTimeFindService(
                new ReservationTimeRepository(jdbcTemplate)
        );
        themeCreateService = new ThemeCreateService(new ThemeRepository(jdbcTemplate));
    }

    @Test
    @DisplayName("날짜와 테마가 주어지면 각 시간의 예약가능 여부를 구한다.")
    void findAvailabilityByDateAndTheme() {
        LocalDate date = LocalDate.now().plusDays(1L);
        ReservationTime reservationTime1 = reservationTimeCreateService.createReservationTime(
                new SaveReservationTimeRequest(LocalTime.now()));
        ReservationTime reservationTime2 = reservationTimeCreateService.createReservationTime(
                new SaveReservationTimeRequest(LocalTime.now().plusHours(1)));
        Theme theme = themeCreateService.createTheme(
                new SaveThemeRequest("capy", "caoyDescription", "caoyThumbnail"));

        reservationCreateService.createReservation(new SaveReservationRequest(
                "capy", date, reservationTime1.getId(), theme.getId()));

        assertThat(reservationTimeFindService.findIsBooked(date, theme.getId()))
                .isEqualTo(Map.of(
                        reservationTime1, true,
                        reservationTime2, false
                        ));
    }
}
