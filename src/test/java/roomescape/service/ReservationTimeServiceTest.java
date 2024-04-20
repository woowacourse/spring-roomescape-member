package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.model.ReservationTime;

class ReservationTimeServiceTest {
    ReservationTimeService reservationTimeService = new ReservationTimeService(new FakeReservationTimeRepository());

    @DisplayName("모든 예약 시간을 반환한다")
    @Test
    void should_return_all_reservation_times() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAllReservationTimes();
        assertThat(reservationTimes).hasSize(2);
    }

    @DisplayName("아이디에 해당하는 예약 시간을 반환한다.")
    @Test
    void should_get_reservation_time() {
        ReservationTime reservationTime = reservationTimeService.findReservationTime(2);
        assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.of(11, 0));
    }

    @DisplayName("예약 시간을 추가한다")
    @Test
    void should_add_reservation_times() {
        ReservationTime reservationTime
                = reservationTimeService.addReservationTime(new ReservationTimeRequest(LocalTime.of(10, 0)));
        List<ReservationTime> allReservationTimes = reservationTimeService.findAllReservationTimes();
        assertThat(allReservationTimes).hasSize(3);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void should_remove_reservation_times() {
        reservationTimeService.deleteReservationTime(1);
        List<ReservationTime> allReservationTimes = reservationTimeService.findAllReservationTimes();
        assertThat(allReservationTimes).hasSize(1);
    }
}
