package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.JdbcThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.repository.JdbcReservationTimeRepository;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;

@JdbcTest
class JdbcReservationRepositoryTest {
    private final JdbcReservationRepository reservationRepository;
    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private ReservationTime time;
    private Theme theme;

    @Autowired
    JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        time = reservationTimeRepository.save(ReservationTimeFixture.DEFAULT_RESERVATION_TIME);
        theme = themeRepository.save(ThemeFixture.DEFAULT_THEME);
    }

    @Test
    void 예약을_저장한다() {
        Reservation reservation = ReservationFixture.reservation("prin", "2024-04-18", time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);

        assertAll(
                () -> assertThat(savedReservation.getName()).isEqualTo("prin"),
                () -> assertThat(savedReservation.getDate()).isEqualTo(LocalDate.parse("2024-04-18")),
                () -> assertThat(savedReservation.getTimeId()).isEqualTo(time.getId()),
                () -> assertThat(savedReservation.getTime()).isEqualTo(time.getStartAt()),
                () -> assertThat(savedReservation.getTheme().getId()).isEqualTo(theme.getId()),
                () -> assertThat(savedReservation.getTheme().getName()).isEqualTo(theme.getName())
        );
    }

    @Test
    void 주어진_예약날짜_시간_테마의_예약이_존재하면_true를_반환한다() {
        String date = "2024-04-18";
        Reservation reservation = ReservationFixture.reservation("prin", date, time, theme);
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.existsByReservationDateTimeAndTheme(LocalDate.parse(date), time.getId(),
                theme.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void 주어진_예약날짜_시간_테마의_예약이_존재하지_않으면_false를_반환한다() {
        boolean exists = reservationRepository.existsByReservationDateTimeAndTheme(LocalDate.parse("2024-04-20"),
                time.getId(), theme.getId());

        assertThat(exists).isFalse();
    }

    @Test
    void 주어진_예약시간의_예약이_존재하면_true를_반환한다() {
        Reservation reservation = ReservationFixture.reservation("prin", "2024-04-18", time, theme);
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.existsByTimeId(time.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void 주어진_예약시간의_예약이_존재하지_않으면_false를_반환한다() {
        boolean exists = reservationRepository.existsByTimeId(0L);

        assertThat(exists).isFalse();
    }

    @Test
    void 주어진_테마의_예약이_존재하면_true를_반환한다() {
        Reservation reservation = ReservationFixture.reservation("prin", "2024-04-18", time, theme);
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.existsByThemeId(theme.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void 주어진_테마의_예약이_존재하지_않으면_false를_반환한다() {
        boolean exists = reservationRepository.existsByThemeId(0L);

        assertThat(exists).isFalse();
    }

    @Test
    void 모든_예약을_조회한다() {
        Reservation reservationPrin = ReservationFixture.reservation("prin", "2024-04-18", time, theme);
        Reservation reservationLiv = ReservationFixture.reservation("liv", "2024-04-19", time, theme);
        reservationRepository.save(reservationPrin);
        reservationRepository.save(reservationLiv);

        List<Reservation> reservations = reservationRepository.findAll();

        assertAll(
                () -> assertThat(reservations).hasSize(2),
                () -> assertThat(reservations.get(0).getName()).isEqualTo("prin"),
                () -> assertThat(reservations.get(0).getTimeId()).isEqualTo(time.getId()),
                () -> assertThat(reservations.get(1).getName()).isEqualTo("liv"),
                () -> assertThat(reservations.get(1).getTimeId()).isEqualTo(time.getId())
        );
    }

    @Test
    void 예약을_삭제한다() {
        Reservation reservation = ReservationFixture.reservation("prin", "2024-04-18", time, theme);
        reservation = reservationRepository.save(reservation);

        int deletedCount = reservationRepository.deleteById(reservation.getId());

        assertThat(deletedCount).isEqualTo(1);
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_0을_반환한다() {
        int deletedCount = reservationRepository.deleteById(0L);

        assertThat(deletedCount).isEqualTo(0);
    }
}
