package roomescape.dao.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.exception.TimeDoesNotExistException;

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
        List<ReservationTime> times = jdbcReservationTimeDao.findAllTimes();

        assertThat(times).hasSize(5);
    }

    @Test
    @DisplayName("주어진 시간과 테마의 예약 시간과 현재 예약 여부를 함께 조회한다")
    void findAllTimesWithBooked() {
        LocalDate date = LocalDate.of(2025, 4, 28);
        Long themeId = 2L;
        List<ReservationTime> times = jdbcReservationTimeDao.findAllTimesWithBooked(date, themeId);

        assertAll(() -> {
            assertThat(times.get(0).getAlreadyBooked()).isTrue();
            assertThat(times.get(1).getAlreadyBooked()).isTrue();
            assertThat(times.get(2).getAlreadyBooked()).isFalse();
            assertThat(times.get(3).getAlreadyBooked()).isFalse();
            assertThat(times.get(4).getAlreadyBooked()).isFalse();
        });
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
        assertThatThrownBy(() -> jdbcReservationTimeDao.findTimeById(100L))
            .isInstanceOf(TimeDoesNotExistException.class)
            .hasMessage("예약 시간을 찾을 수 없다.");
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
        Long id = newReservationTime.getId();

        jdbcReservationTimeDao.removeTimeById(id);

        assertThatThrownBy(() -> jdbcReservationTimeDao.findTimeById(id))
            .isInstanceOf(TimeDoesNotExistException.class);
    }

    @Test
    @DisplayName("시간이 존재하는지 확인할 수 있다.")
    void existTimeByStartAt() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTime reservationTime = new ReservationTime(null, startAt);
        jdbcReservationTimeDao.addTime(reservationTime);
        assertThat(jdbcReservationTimeDao.existTimeByStartAt(startAt)).isTrue();
    }
}
