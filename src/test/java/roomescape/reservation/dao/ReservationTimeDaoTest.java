package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.model.ReservationTime;

@JdbcTest
public class ReservationTimeDaoTest {

    private ReservationTimeDao reservationTimeDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationTimeDaoTest(JdbcTemplate jdbcTemplate) {
        this.reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("시간 추가 확인 테스트")
    void insertTest() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 40));

        // when
        reservationTimeDao.insert(reservationTime);

        // then
        assertThat(count()).isEqualTo(count());
    }

    @Test
    @DisplayName("시간 삭제 확인 테스트")
    void deleteTest() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 40));
        reservationTimeDao.insert(reservationTime);

        // when
        reservationTimeDao.delete(1L);

        // then
        assertThat(count()).isEqualTo(0);
    }

    private int count() {
        String sql = "select count(*) from reservation_time";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
