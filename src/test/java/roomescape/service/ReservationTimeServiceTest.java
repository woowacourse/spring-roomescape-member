package roomescape.service;

import java.time.LocalTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;

import static org.assertj.core.api.Assertions.*;

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
                = reservationTimeService.addReservationTime(new ReservationTimeRequest("10:00"));
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

    @DisplayName("존재하지 않는 시간이면 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_not_exist_id() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(10000000))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] 존재하지 않는 시간입니다.");
    }

    @DisplayName("존재하는 시간이면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_exist_id() {
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1))
                .doesNotThrowAnyException();
    }
}
