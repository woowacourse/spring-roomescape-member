package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.model.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
