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
import roomescape.service.reservation.ReservationTime;

@JdbcTest
class ReservationDaoTest {

    private H2ReservationDao reservationDao;
    private H2ReservationTimeDao reservationTimeDao;
    private final LocalTime time = LocalTime.of(10, 0);
    private ReservationTime reservationTime;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        reservationDao = new H2ReservationDao(jdbcTemplate, dataSource);
        reservationTimeDao = new H2ReservationTimeDao(jdbcTemplate, dataSource);
        jdbcTemplate.execute("DROP TABLE reservation IF EXISTS");
        jdbcTemplate.execute("DROP TABLE reservation_time IF EXISTS");
        jdbcTemplate.execute("""
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
                    PRIMARY KEY (id),
                    FOREIGN KEY (time_id) REFERENCES reservation_time (id)
                );
                """);
        reservationTime = reservationTimeDao.save(new ReservationTime(time));
    }

    @DisplayName("새로운 예약을 생성할 수 있다.")
    @Test
    void testCreateReservation() {
        // given
        String name = "leo";
        LocalDate date = LocalDate.of(2025, 9, 24);
        // when
        Reservation reservation = reservationDao.save(
                new Reservation(null, name, date, new ReservationTime(1L, LocalTime.now())));
        // then
        assertThat(reservation).isEqualTo(
                new Reservation(1L, name, date, new ReservationTime(1L, LocalTime.of(10, 0))));
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void testDeleteReservation() {
        // given
        String name = "leo";
        LocalDate date = LocalDate.of(2025, 9, 24);
        reservationDao.save(new Reservation(null, name, date, new ReservationTime(1L, LocalTime.now())));
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
        Reservation reservation = reservationDao.save(
                new Reservation(null, name, date, new ReservationTime(1L, LocalTime.now())));
        // when
        // then
        assertThat(reservationDao.findAll()).containsExactly(reservation);
    }
}
