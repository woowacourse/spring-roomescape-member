package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@JdbcTest
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDaoTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private final LocalTime time = LocalTime.of(10, 0);
    private Theme theme = new Theme(1L, "우테코방탈출", "탈출탈출탈출", "abcdefg");;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        reservationDao = new H2ReservationDao(namedParameterJdbcTemplate);
        reservationTimeDao = new H2ReservationTimeDao(namedParameterJdbcTemplate);
        themeDao = new H2ThemeDao(namedParameterJdbcTemplate);
        reservationTimeDao.save(new ReservationTime(time));
        themeDao.save(theme);
    }

    @DisplayName("새로운 예약을 생성할 수 있다.")
    @Test
    void testCreateReservation() {
        // given
        String name = "leo";
        LocalDate date = LocalDate.of(2025, 9, 24);
        ReservationTime time = new ReservationTime(1L, LocalTime.now());

        // when
        Reservation result = reservationDao.save(
                new Reservation(null, name, date, time, theme));
        // then
        Reservation savedReservation = reservationDao.findAll().getFirst();
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo(name),
                () -> assertThat(result.getDate()).isEqualTo(date),
                () -> assertThat(result.getTime()).isEqualTo(time),
                () -> assertThat(result.getTheme()).isEqualTo(theme),
                () -> assertThat(savedReservation.getId()).isEqualTo(1L),
                () -> assertThat(savedReservation.getName()).isEqualTo(name),
                () -> assertThat(savedReservation.getDate()).isEqualTo(date),
                () -> assertThat(savedReservation.getTime()).isEqualTo(time),
                () -> assertThat(savedReservation.getTheme()).isEqualTo(theme)
        );
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void testDeleteReservation() {
        // given
        String name = "leo";
        LocalDate date = LocalDate.of(2025, 9, 24);
        Theme theme = new Theme(1L, "우테코방탈출", "탈출탈출탈출", "abcdefg");
        reservationDao.save(new Reservation(null, name, date, new ReservationTime(1L, LocalTime.now()), theme));
        // when
        reservationDao.deleteById(1L);
        // then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("예약 목록을 조회할 수 있다.")
    @Test
    void testGetReservations() {
        // given
        String name = "leo";
        LocalDate date = LocalDate.of(2025, 9, 24);
        Theme theme = new Theme(1L, "우테코방탈출", "탈출탈출탈출", "abcdefg");
        Reservation reservation = reservationDao.save(
                new Reservation(null, name, date, new ReservationTime(1L, LocalTime.now()), theme));
        // when
        // then
        assertThat(reservationDao.findAll()).containsExactly(reservation);
    }
}
