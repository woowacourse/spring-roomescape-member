package roomescape.reservation.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.ReservationTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.ReservationTimeFixture.reservationTimeFixture;

@JdbcTest
class JdbcReservationTimeDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcReservationTimeDao jdbcReservationTimeDao;

    @Autowired
    private JdbcReservationTimeDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcReservationTimeDao = new JdbcReservationTimeDao(jdbcTemplate);
    }

    @AfterEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN `id` RESTART");
    }

    @DisplayName("DB 시간 추가 테스트")
    @Test
    void save() {
        jdbcReservationTimeDao.save(reservationTimeFixture);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("DB 모든 시간 조회 테스트")
    @Test
    void findAllReservationTimes() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", reservationTimeFixture.getStartAt());
        List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAllReservationTimes();
        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("DB 시간 삭제 테스트")
    @Test
    void delete() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", reservationTimeFixture.getStartAt());
        jdbcReservationTimeDao.delete(1L);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
