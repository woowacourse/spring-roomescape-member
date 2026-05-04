package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        executeSchema();
    }

    private void executeSchema() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS reservation_time (id BIGINT AUTO_INCREMENT PRIMARY KEY, start_at TIME)");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    @DisplayName("예약 시간을 저장하고 영속화된 객체를 반환한다.")
    void save() {
        ReservationTime reservationTime = ReservationTime.transientOf(LocalTime.of(10, 0));
        ReservationTime savedTime = reservationTimeDao.save(reservationTime);
        assertThat(savedTime.id()).isPositive();
    }

    @Test
    @DisplayName("식별자로 예약 시간 객체를 조회한다.")
    void findById() {
        ReservationTime savedTime = reservationTimeDao.save(ReservationTime.transientOf(LocalTime.of(10, 0)));
        ReservationTime foundTime = reservationTimeDao.findById(savedTime.id());
        assertThat(foundTime.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("모든 예약 시간 객체 목록을 조회한다.")
    void findAll() {
        reservationTimeDao.save(ReservationTime.transientOf(LocalTime.of(10, 0)));
        List<ReservationTime> times = reservationTimeDao.findAll();
        assertThat(times).hasSize(1);
    }

    @Test
    @DisplayName("식별자로 예약 시간을 삭제한다.")
    void deleteById() {
        ReservationTime savedTime = reservationTimeDao.save(ReservationTime.transientOf(LocalTime.of(10, 0)));
        reservationTimeDao.deleteById(savedTime.id());
        assertThat(reservationTimeDao.findAll()).isEmpty();
    }
}
