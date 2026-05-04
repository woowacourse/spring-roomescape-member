package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationDao reservationDao;
    private ReservationTime savedTime;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
        executeSchema();
        insertDependencyData();
    }

    private void executeSchema() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reservation_time (id BIGINT AUTO_INCREMENT PRIMARY KEY, start_at TIME)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reservation (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), date DATE, time_id BIGINT)");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void insertDependencyData() {
        ReservationTimeDao timeDao = new ReservationTimeDao(jdbcTemplate);
        savedTime = timeDao.save(ReservationTime.transientOf(LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("예약을 저장하고 영속화된 객체를 반환한다.")
    void save() {
        Reservation reservation = Reservation.transientOf("브라운", LocalDate.now(), savedTime);
        Reservation savedReservation = reservationDao.save(reservation);
        assertThat(savedReservation.id()).isPositive();
    }

    @Test
    @DisplayName("식별자로 예약 객체를 조회한다.")
    void findById() {
        Reservation savedReservation = reservationDao.save(Reservation.transientOf("브라운", LocalDate.now(), savedTime));
        Reservation foundReservation = reservationDao.findById(savedReservation.id());
        assertThat(foundReservation.name()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("모든 예약 객체 목록을 조회한다.")
    void findAll() {
        reservationDao.save(Reservation.transientOf("브라운", LocalDate.now(), savedTime));
        List<Reservation> reservations = reservationDao.findAll();
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("식별자로 예약을 삭제한다.")
    void deleteById() {
        Reservation savedReservation = reservationDao.save(Reservation.transientOf("브라운", LocalDate.now(), savedTime));
        reservationDao.deleteById(savedReservation.id());
        assertThat(reservationDao.findAll()).isEmpty();
    }
}
