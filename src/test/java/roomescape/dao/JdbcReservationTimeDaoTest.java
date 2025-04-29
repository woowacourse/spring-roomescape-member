package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.jdbc.JdbcReservationTimeDao;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import(JdbcReservationTimeDao.class)
public class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;

    @Test
    @DisplayName("전체 시간을 조회할 수 있다.")
    void findAllReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('15:40')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:00')");

        List<ReservationTime> times = jdbcReservationTimeDao.findAllTimes();

        assertThat(times).hasSize(2);
    }

    @Test
    @DisplayName("ID로 시간이 존재한다면 조회할 수 있다.")
    void findTimeByExistedTime() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(12, 0));
        ReservationTime actual = jdbcReservationTimeDao.addTime(reservationTime);

        ReservationTime expected = jdbcReservationTimeDao.findTimeById(actual.getId());

        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getStartAt()).isEqualTo(expected.getStartAt());
        });
    }

    @Test
    @DisplayName("ID로 시간이 존재하지 않는다면 예외가 발생한다.")
    void findTimeByNotExistedTime() {
        assertThatThrownBy(() -> jdbcReservationTimeDao.findTimeById(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 가능한 시간이 아닙니다.");
    }

    @Test
    @DisplayName("시간을 추가할 수 있다.")
    void addReservationTime() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(12, 0));
        ReservationTime newReservationTime = jdbcReservationTimeDao.addTime(reservationTime);

        assertThat(newReservationTime).isNotNull();
    }

    @Test
    @DisplayName("ID로 시간을 삭제할 수 있다.")
    void removeReservation() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(12, 0));
        ReservationTime newReservationTime = jdbcReservationTimeDao.addTime(reservationTime);

        jdbcReservationTimeDao.removeTimeById(newReservationTime.getId());

        assertThat(jdbcReservationTimeDao.findAllTimes()).isEmpty();
    }

    @Test
    @DisplayName("시간이 존재하는지 확인할 수 있다.")
    void existTimeByStartAt() {
        LocalTime startAt = LocalTime.of(12, 0);
        ReservationTime reservationTime = new ReservationTime(null, startAt);
        jdbcReservationTimeDao.addTime(reservationTime);
        assertThat(jdbcReservationTimeDao.existTimeByStartAt(startAt)).isTrue();
    }
}
