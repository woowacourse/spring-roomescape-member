package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeDAOTest {

    @Autowired
    ReservationTimeDAO reservationTimeDAO;

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void insert() {
        final ReservationTime savedReservationTime = reservationTimeDAO.insert(new ReservationTime(LocalTime.now()));

        assertThat(savedReservationTime).isNotNull();
    }

    @Test
    @DisplayName("전체 예약 시간을 조회한다.")
    void selectAll() {
        reservationTimeDAO.insert(new ReservationTime(LocalTime.now()));

        final List<ReservationTime> reservationTimes = reservationTimeDAO.selectAll();

        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteById() {
        reservationTimeDAO.insert(new ReservationTime(LocalTime.now()));

        reservationTimeDAO.deleteById(1L);
        final List<ReservationTime> reservationTimes = reservationTimeDAO.selectAll();

        assertThat(reservationTimes).hasSize(0);
    }

    @Test
    @DisplayName("입력된 시간에 예약이 존재하는지 여부를 알 수 있다.")
    void existReservationOfTime_true() {
        final LocalTime time = LocalTime.now();
        reservationTimeDAO.insert(new ReservationTime(time));

        final Boolean result = reservationTimeDAO.existReservationOfTime(time);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("입력된 시간에 예약이 존재하는지 여부를 알 수 있다.")
    void existReservationOfTime_false() {
        final LocalTime time = LocalTime.now();
        reservationTimeDAO.insert(new ReservationTime(time));

        final Boolean result = reservationTimeDAO.existReservationOfTime(time.minusHours(1));

        assertThat(result).isFalse();
    }
}
