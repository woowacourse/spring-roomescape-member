package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

class ReservationServiceTest {

    private final FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();

    private final ReservationService reservationService = new ReservationService(
            new FakeReservationRepository(),
            reservationTimeRepository,
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
                new ReservationRequest("네오", LocalDate.of(2030, 1, 1), 1L, 1L));
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
                .hasMessage("[ERROR] 해당 id:[100000000] 값으로 예약된 내역이 존재하지 않습니다.");
    }

    @DisplayName("존재하는 예약을 삭제하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_exist_reservation_time() {
        assertThatCode(() -> reservationService.deleteReservation(1))
                .doesNotThrowAnyException();
    }

    @DisplayName("현재 이전으로 예약하면 예외가 발생한다.")
    @Test
    void should_throw_exception_when_previous_date() {
        ReservationRequest request = new ReservationRequest("에버", LocalDate.of(2000, 1, 11), 1L, 1L);
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("[ERROR] 현재(",") 이전 시간으로 예약할 수 없습니다.");
    }

    @DisplayName("현재로 예약하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_current_date() {
        reservationTimeRepository.add(new ReservationTime(3, LocalTime.now()));
        ReservationRequest request = new ReservationRequest("에버", LocalDate.now(), 3L, 1L);
        assertThatCode(() -> reservationService.addReservation(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("현재 이후로 예약하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_later_date() {
        ReservationRequest request = new ReservationRequest("에버", LocalDate.of(2030, 1, 11), 1L, 1L);
        assertThatCode(() -> reservationService.addReservation(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("날짜, 시간이 일치하는 예약을 추가하려 할 때 예외가 발생한다.")
    @Test
    void should_throw_exception_when_add_exist_reservation() {
        ReservationRequest request = new ReservationRequest("배키", LocalDate.of(2030, 8, 5), 2L, 2L);
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage("[ERROR] 이미 해당 시간(2030-08-05T11:00)에 예약이 존재합니다.");
    }

    @DisplayName("예약 가능 상태를 담은 시간 정보를 반환한다.")
    @Test
    void should_return_times_with_book_state() {
        List<MemberReservationTimeResponse> times = reservationService.getMemberReservationTimes(
                LocalDate.of(2030, 8, 5), 1);
        assertThat(times).hasSize(2);
        assertThat(times).containsOnly(
                new MemberReservationTimeResponse(1, LocalTime.of(10, 0), false),
                new MemberReservationTimeResponse(2, LocalTime.of(11, 0), true)
        );
    }
}
