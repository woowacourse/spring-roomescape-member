package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.ReservationTime;

@JdbcTest
class JdbcReservationTimeDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcReservationTimeDao jdbcReservationTimeDao;

    private ReservationTime savedReservationTime;

    @Autowired
    private JdbcReservationTimeDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcReservationTimeDao = new JdbcReservationTimeDao(jdbcTemplate);
    }

    @BeforeEach
    void saveReservationTime() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(10, 0));
        savedReservationTime = jdbcReservationTimeDao.save(reservationTime);
    }

    @AfterEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN `id` RESTART");
    }

    @DisplayName("DB 시간 추가 테스트")
    @Test
    void save() {
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("DB 모든 시간 조회 테스트")
    @Test
    void findAllReservationTimes() {
        List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAllReservationTimes();

        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("DB 시간 삭제 테스트")
    @Test
    void delete() {
        jdbcReservationTimeDao.delete(savedReservationTime.getId());
        List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAllReservationTimes();

        assertThat(reservationTimes).isEmpty();
    }
}
