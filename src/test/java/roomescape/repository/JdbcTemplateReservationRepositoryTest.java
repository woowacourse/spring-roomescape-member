package roomescape.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcTemplateReservationRepository.class, JdbcTemplateThemeRepository.class})
class JdbcTemplateReservationRepositoryTest {

    private static final long TIME_ID = 1L;
    private static final long THEME_ID = 1L;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql({"/test-truncate.sql", "/test-theme.sql", "/test-reservation-time.sql"})
    void 예약을_저장하면_id가_채워진_도메인을_반환한다() {
        Reservation saved = addReservation("브라운", LocalDate.of(2026, 5, 3));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.name()).isEqualTo("브라운");
        assertThat(saved.date()).isEqualTo(LocalDate.of(2026, 5, 3));
        assertThat(saved.time().id()).isEqualTo(TIME_ID);
    }

    private Reservation addReservation(String name, LocalDate date) {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = themeRepository.findById(THEME_ID).get();
        return reservationRepository.addReservation(new Reservation(null, name, date, time, theme));
    }

    @Test
    @Sql({"/test-truncate.sql", "/test-theme.sql", "/test-reservation-time.sql"})
    void 모든_예약을_조인_조회한다() {
        addReservation("브라운", LocalDate.of(2026, 5, 3));
        addReservation("조이", LocalDate.of(2026, 5, 4));

        List<Reservation> reservations = reservationRepository.findAllReservations();

        assertThat(reservations).hasSize(2);
        assertThat(reservations.get(0).time().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @Sql({"/test-truncate.sql", "/test-theme.sql", "/test-reservation-time.sql"})
    void 특정_사용자의_예약을_조회한다() {
        addReservation("브라운", LocalDate.of(2026, 5, 3));
        addReservation("브라운", LocalDate.of(2026, 5, 4));
        addReservation("조이", LocalDate.of(2026, 5, 5));

        List<Reservation> reservations = reservationRepository.findReservationsByName("브라운");

        assertThat(reservations).hasSize(2);
    }

    @Test
    @Sql({"/test-truncate.sql", "/test-theme.sql", "/test-reservation-time.sql"})
    void 예약이_없으면_빈_리스트를_반환한다() {
        List<Reservation> reservations = reservationRepository.findAllReservations();

        assertThat(reservations).isEmpty();
    }

    @Test
    @Sql({"/test-truncate.sql", "/test-theme.sql", "/test-reservation-time.sql"})
    void id로_예약을_삭제한다() {
        long reservationId = addReservation("브라운", LocalDate.of(2026, 5, 3)).id();

        reservationRepository.deleteById(reservationId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
