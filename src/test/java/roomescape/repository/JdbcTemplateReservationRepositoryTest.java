package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
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
@Sql({"/test-theme.sql", "/test-reservation-time.sql"})
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

    private Reservation addReservation(String name, LocalDate date) {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = themeRepository.findById(THEME_ID).get();
        return reservationRepository.addReservation(new Reservation(null, name, date, time, theme));
    }

    @Test
    @DisplayName("예약을 저장하면 id가 포함된 예약을 반환한다")
    void saveReservation() {
        Reservation saved = addReservation("브라운", LocalDate.of(2026, 5, 3));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.name()).isEqualTo("브라운");
        assertThat(saved.date()).isEqualTo(LocalDate.of(2026, 5, 3));
        assertThat(saved.time().id()).isEqualTo(TIME_ID);
    }

    @Test
    @DisplayName("모든 예약을 시간과 테마 정보와 함께 조회한다")
    void findAllReservations() {
        addReservation("브라운", LocalDate.of(2026, 5, 3));
        addReservation("조이", LocalDate.of(2026, 5, 4));

        List<Reservation> reservations = reservationRepository.findAllReservations();

        assertThat(reservations).hasSize(2);
        assertThat(reservations.get(0).time().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("사용자 이름으로 예약을 조회한다")
    void findReservationsByName() {
        addReservation("브라운", LocalDate.of(2026, 5, 3));
        addReservation("브라운", LocalDate.of(2026, 5, 4));
        addReservation("조이", LocalDate.of(2026, 5, 4));

        List<Reservation> reservations = reservationRepository.findReservationsByName("브라운");

        assertThat(reservations).hasSize(2);
    }

    @Test
    @DisplayName("예약이 없으면 빈 목록을 반환한다")
    void findEmptyReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();

        assertThat(reservations).isEmpty();
    }

    @Test
    @DisplayName("id로 예약을 삭제한다")
    void deleteReservationById() {
        long reservationId = addReservation("브라운", LocalDate.of(2026, 5, 3)).id();

        reservationRepository.deleteById(reservationId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
