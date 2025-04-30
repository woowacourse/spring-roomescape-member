package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;
import roomescape.service.reservation.Theme;

@JdbcTest
class ReservationDaoTest {

    private H2ReservationDao reservationDao;
    private H2ReservationTimeDao reservationTimeDao;
    private H2ThemeDao themeDao;
    private final LocalTime time = LocalTime.of(10, 0);
    private ReservationTime reservationTime;
    private Theme theme;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        reservationDao = new H2ReservationDao(jdbcTemplate, dataSource);
        reservationTimeDao = new H2ReservationTimeDao(jdbcTemplate, dataSource);
        themeDao = new H2ThemeDao(jdbcTemplate, dataSource);
        jdbcTemplate.execute("DROP TABLE reservation IF EXISTS");
        jdbcTemplate.execute("DROP TABLE reservation_time IF EXISTS");
        jdbcTemplate.execute("DROP TABLE theme IF EXISTS");
        jdbcTemplate.execute("""            
                CREATE TABLE theme
                (
                    id          BIGINT       NOT NULL AUTO_INCREMENT,
                    name        VARCHAR(255) NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    thumbnail   VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                CREATE TABLE reservation_time
                (
                    id       BIGINT       NOT NULL AUTO_INCREMENT,
                    start_at VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                CREATE TABLE reservation
                (
                    id      BIGINT       NOT NULL AUTO_INCREMENT,
                    name    VARCHAR(255) NOT NULL,
                    date    VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    theme_id BIGINT,
                    PRIMARY KEY (id),
                    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
                    FOREIGN KEY (theme_id) REFERENCES theme (id)
                );
                """);
        reservationTime = reservationTimeDao.save(new ReservationTime(time));
        theme = new Theme(1L, "우테코방탈출", "탈출탈출탈출", "abcdefg");
        themeDao.save(theme);
    }

    @DisplayName("새로운 예약을 생성할 수 있다.")
    @Test
    void testCreateReservation() {
        // given
        String name = "leo";
        LocalDate date = LocalDate.of(2025, 9, 24);

        // when
        Reservation reservation = reservationDao.save(
                new Reservation(null, name, date, new ReservationTime(1L, LocalTime.now()), theme));
        // then
        assertThat(reservation).isEqualTo(
                new Reservation(1L, name, date, new ReservationTime(1L, LocalTime.of(10, 0)), theme));
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
