package roomescape.reservation.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.domain.Reservation;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    private final ReservationTime reservationTime = new ReservationTime(1L, "10:00");
    private final Reservation reservation = new Reservation(1L, "hotea", "2024-04-11", reservationTime);

    @Test
    @DisplayName("특정 예약을 생성할 수 있다.")
    void save() {
        reservationTimeDao.save(reservationTime);
        assertThat(reservationDao.save(reservation)).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약을 모두 조회할 수 있다.")
    void findAll() {
        save();
        List<Reservation> reservationList = reservationDao.findAll();
        assertAll(
                () -> assertThat(reservationList.get(0)).isEqualTo(reservation),
                () -> assertThat(reservationList.size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("특정 예약을 조회할 수 있다.")
    void findById() {
        save();
        assertThat(reservationDao.findById(1L)).isEqualTo(reservation);
    }

    @Test
    @DisplayName("특정 예약을 삭제 할 수 있다.")
    void deleteById() {
        save();
        assertThat(reservationDao.deleteById(1L)).isEqualTo(1);
    }
}
