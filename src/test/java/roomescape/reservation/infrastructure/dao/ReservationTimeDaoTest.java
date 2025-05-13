package roomescape.reservation.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.application.exception.DeleteTimeException;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;

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
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));

        // when
        reservationTimeDao.insert(reservationTimeRequest.getStartAt());

        // then
        assertThat(count()).isEqualTo(count());
    }

    @Test
    @DisplayName("시간 전체 조회 테스트")
    void findAllTimesTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeDao.insert(reservationTimeRequest.getStartAt());

        // when
        assertThat(reservationTimeDao.findAllTimes()).hasSize(1);
    }

    @Test
    @DisplayName("시간 삭제 확인 테스트")
    void deleteTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeDao.insert(reservationTimeRequest.getStartAt());

        // when
        reservationTimeDao.delete(1L);

        // then
        assertThat(count()).isEqualTo(0);
    }

    @Test
    @DisplayName("저장되어 있지 않은 id로 요청을 보내면 예외가 발생한다.")
    void deleteExceptionTest() {
        assertThatThrownBy(() -> reservationTimeDao.delete(1L))
                .isInstanceOf(DeleteTimeException.class)
                .hasMessage("[ERROR] 삭제하지 못했습니다.");
    }

    private int count() {
        String sql = "select count(*) from reservation_time";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
