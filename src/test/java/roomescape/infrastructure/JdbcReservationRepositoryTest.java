package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.fixture.ReservationFixture;

@JdbcTest
@Sql("/reservation.sql")
class JdbcReservationRepositoryTest {
    private final JdbcReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    void 예약을_저장한다() {
        ReservationTime time = reservationTimeRepository.findById(1L).orElseThrow();
        Theme theme = themeRepository.findById(1L).orElseThrow();
        Reservation reservation = ReservationFixture.reservation("prin", "2024-04-18", time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);

        assertAll(
                () -> assertThat(savedReservation.getName()).isEqualTo(reservation.getName()),
                () -> assertThat(savedReservation.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(savedReservation.getTimeId()).isEqualTo(time.getId()),
                () -> assertThat(savedReservation.getTime()).isEqualTo(time.getStartAt()),
                () -> assertThat(savedReservation.getTheme().getId()).isEqualTo(theme.getId()),
                () -> assertThat(savedReservation.getTheme().getName()).isEqualTo(theme.getName())
        );
    }

    @Test
    void 주어진_예약날짜_시간_테마의_예약이_존재하면_true를_반환한다() {
        LocalDate date = LocalDate.parse("2024-05-01");
        boolean exists = reservationRepository.existsByReservationDateTimeAndTheme(date, 1L, 1L);

        assertThat(exists).isTrue();
    }

    @Test
    void 주어진_예약날짜_시간_테마의_예약이_존재하지_않으면_false를_반환한다() {
        LocalDate date = LocalDate.parse("2024-04-20");
        boolean exists = reservationRepository.existsByReservationDateTimeAndTheme(date, 1L, 1L);

        assertThat(exists).isFalse();
    }

    @Test
    void 주어진_예약시간의_예약이_존재하면_true를_반환한다() {
        boolean exists = reservationRepository.existsByTimeId(1L);

        assertThat(exists).isTrue();
    }

    @Test
    void 주어진_예약시간의_예약이_존재하지_않으면_false를_반환한다() {
        boolean exists = reservationRepository.existsByTimeId(0L);

        assertThat(exists).isFalse();
    }

    @Test
    void 주어진_테마의_예약이_존재하면_true를_반환한다() {
        boolean exists = reservationRepository.existsByThemeId(1L);

        assertThat(exists).isTrue();
    }

    @Test
    void 주어진_테마의_예약이_존재하지_않으면_false를_반환한다() {
        boolean exists = reservationRepository.existsByThemeId(0L);

        assertThat(exists).isFalse();
    }

    @Test
    void 모든_예약을_조회한다() {
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).hasSize(10);
    }

    @Test
    void 예약을_삭제한다() {
        boolean isDeleted = reservationRepository.deleteById(2L);

        assertThat(isDeleted).isTrue();
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_0을_반환한다() {
        boolean isDeleted = reservationRepository.deleteById(0L);

        assertThat(isDeleted).isFalse();
    }
}
