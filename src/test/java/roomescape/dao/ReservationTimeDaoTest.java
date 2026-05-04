package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;

public class ReservationTimeDaoTest extends BaseDaoTest {
    private ReservationTimeDao reservationTimeDao;

    @Override
    protected void initTable() {
        createReservationTimeTable();
        insertReservationTime("10:00");

        this.reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationTimeTable();
    }

    @Test
    @DisplayName("특정 예약 시간 정상적으로 가져오는 지 테스트")
    void getReservationTimeTest() {
        Optional<ReservationTime> reservationTime = reservationTimeDao.getReservationTime(1);

        assertThat(reservationTime.isPresent()).isTrue();
        assertThat(reservationTime.get()).isEqualTo(new ReservationTime(1, "10:00"));
    }

    @Test
    @DisplayName("존재하지 않는 특정 예약 시간 빈 값으로 가져오는 지 테스트")
    void getInvalidReservationTimeTest() {
        Optional<ReservationTime> reservationTime = reservationTimeDao.getReservationTime(3);

        assertThat(reservationTime.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("전체 예약시간 정상적으로 가져오는 지 테스트")
    void getReservationTimesTest() {
        List<ReservationTime> reservationTimes = reservationTimeDao.getAllReservationTime();

        assertThat(reservationTimes).containsExactly(new ReservationTime(1, "10:00"));
    }

    @Test
    @DisplayName("예약 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationTimeDao.deleteReservationTime(1);
        List<ReservationTime> reservationTimes = reservationTimeDao.getAllReservationTime();

        assertThat(reservationTimes).isNotIn(new ReservationTime(1, "10:00"));
    }

    @Test
    @DisplayName("예약 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        long updatedReservation = reservationTimeDao.insertReservationTime(new ReservationTimeCommand("12:00"));
        List<ReservationTime> reservations = reservationTimeDao.getAllReservationTime();

        ReservationTime expectedReservation = new ReservationTime(2, "12:00");

        assertThat(updatedReservation).isEqualTo(2);
        assertThat(reservations).contains(expectedReservation);
    }
}
