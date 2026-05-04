package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;

public class ReservationDaoTest extends BaseDaoTest {
    private ReservationDao reservationDao;

    private final Reservation INIT_RESERVATION = new Reservation(1, "브라운", "2023-08-05", new ReservationTime(1, "10:00"));

    @Override
    protected void initTable() {
        createReservationTimeTable();
        createReservationTable();

        insertReservationTime("10:00");
        insertReservation("브라운", "2023-08-05", 1);
        this.reservationDao = new ReservationDao(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationTable();
        deleteReservationTimeTable();
    }

    @Test
    @DisplayName("전체 예약 테스트 정상적으로 가져오는 지 테스트")
    void getReservationTest() {
        List<Reservation> reservations = reservationDao.getAllReservation();

        assertThat(reservations).containsExactly(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationDao.deleteReservation(1);
        List<Reservation> reservations = reservationDao.getAllReservation();

        assertThat(reservations).isNotIn(INIT_RESERVATION);
    }

    @Test
    @DisplayName("예약 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        long updatedReservation = reservationDao.insertReservation(new ReservationCommand("테스트", "2023-08-15", 1));
        List<Reservation> reservations = reservationDao.getAllReservation();

        Reservation expectedReservation = new Reservation(2, "테스트", "2023-08-15", new ReservationTime(1, "10:00"));

        assertThat(updatedReservation).isEqualTo(2);
        assertThat(reservations).contains(expectedReservation);
    }
}
