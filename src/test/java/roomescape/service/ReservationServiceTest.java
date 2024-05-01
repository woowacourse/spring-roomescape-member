package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationServiceTest {
    ReservationService reservationService = new ReservationService(
            new FakeReservationRepository(),
            new FakeReservationTimeRepository(),
            new FakeThemeRepository());

    @DisplayName("모든 예약 시간을 반환한다")
    @Test
    void should_return_all_reservation_times() {
        List<Reservation> reservations = reservationService.findAllReservations();
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("예약 시간을 추가한다")
    @Test
    void should_add_reservation_times() {
        reservationService.addReservation(
                new ReservationRequest("2030-01-01", "네오", 1, 1));
        List<Reservation> allReservations = reservationService.findAllReservations();
        assertThat(allReservations).hasSize(3);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void should_remove_reservation_times() {
        reservationService.deleteReservation(1);
        List<Reservation> allReservations = reservationService.findAllReservations();
        assertThat(allReservations).hasSize(1);
    }

    @DisplayName("존재하지 않는 예약을 삭제하면 예외가 발생한다.")
    @Test
    void should_throw_exception_when_not_exist_reservation_time() {
        assertThatThrownBy(() -> reservationService.deleteReservation(100000000))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] 존재하지 않는 예약입니다.");
    }

    @DisplayName("존재하는 예약을 삭제하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_exist_reservation_time() {
        assertThatCode(() -> reservationService.deleteReservation(1))
                .doesNotThrowAnyException();
    }

    //todo 현재 시간에 따라 테스트 깨짐 + 경계값 테스트
    @DisplayName("현재 이전으로 예약하면 예외가 발생한다.")
    @Test
    void should_throw_exception_when_previous_date() {
        ReservationRequest request = new ReservationRequest("2000-01-11", "에버", 1, 1);
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 현재 이전 예약은 할 수 없습니다.");
    }

    @DisplayName("현재 이후로 예약하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_later_date() {
        ReservationRequest request = new ReservationRequest("2030-01-11", "에버", 1, 1);
        assertThatCode(() -> reservationService.addReservation(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("날짜, 시간이 일치하는 예약을 추가하려 할 때 예외가 발생한다.")
    @Test
    void should_throw_exception_when_add_exist_reservation() {
        ReservationRequest request = new ReservationRequest("2030-08-05", "배키", 2, 2);
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage("[ERROR] 중복되는 예약은 추가할 수 없습니다.");
    }
}
