package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.controller.request.ReservationTimeRequest;
import roomescape.controller.response.IsReservedTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

class ReservationTimeServiceTest {

    private final ReservationTimeService reservationTimeService = new ReservationTimeService(
            new ReservationTimeRepository(new FakeReservationDao(), new FakeReservationTimeDao())
    );

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
        reservationTimeService.addReservationTime(new ReservationTimeRequest(LocalTime.of(12, 0)));
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
                .hasMessage("[ERROR] id(10000000)에 해당하는 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("존재하는 시간이면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_exist_id() {
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1))
                .doesNotThrowAnyException();
    }

    @DisplayName("특정 시간에 대핸 예약이 존재하는데, 그 시간을 삭제하려 할 때 예외가 발생한다.")
    @Test
    void should_throw_exception_when_exist_reservation_using_time() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(2))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
    }

    @DisplayName("존재하는 시간을 추가하려 할 때 예외가 발생한다.")
    @Test
    void should_throw_exception_when_add_exist_time() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        assertThatThrownBy(() -> reservationTimeService.addReservationTime(request))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage("[ERROR] 이미 존재하는 시간입니다.");
    }

    @DisplayName("예약 가능 상태를 담은 시간 정보를 반환한다.")
    @Test
    void should_return_times_with_book_state() {
        List<IsReservedTimeResponse> times = reservationTimeService.getIsReservedTime(
                LocalDate.of(2030, 8, 5), 1);
        assertThat(times).hasSize(2);
        assertThat(times).containsOnly(
                new IsReservedTimeResponse(1, LocalTime.of(10, 0), false),
                new IsReservedTimeResponse(2, LocalTime.of(11, 0), true)
        );
    }
}
