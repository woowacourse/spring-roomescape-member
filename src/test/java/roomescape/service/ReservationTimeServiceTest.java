package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeAddRequest;

class ReservationTimeServiceTest {

    @DisplayName("모든 예약 시간을 찾습니다")
    @Test
    void findAllReservationTime() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(new FakeReservationTimeDao(
                Arrays.asList(new ReservationTime(1L, LocalTime.of(10, 0)))
        ));
        int expectedSize = 1;

        int actualSize = reservationTimeService.findAllReservationTime().size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @DisplayName("예약시간을 추가하고 저장된 예약시간을 반환합니다.")
    @Test
    void should_add_reservation_time() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(new FakeReservationTimeDao());
        ReservationTime expectedReservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        ReservationTime actualReservationTime = reservationTimeService.addReservationTime(new ReservationTimeAddRequest(
                LocalTime.of(10, 0)
        ));

        assertThat(actualReservationTime).isEqualTo(expectedReservationTime);
    }

    @DisplayName("중복되는 예약 시각을 추가할 경우 예외가 발생합니다.")
    @Test
    void should_throw_IllegalArgumentException_when_reservation_time_is_duplicated() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(new FakeReservationTimeDao(
                Arrays.asList(new ReservationTime(1L, LocalTime.of(10, 0)))
        ));

        ReservationTimeAddRequest reservationTimeAddRequest = new ReservationTimeAddRequest(LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationTimeService.addReservationTime(reservationTimeAddRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약시간은 추가할 수 없습니다.");
    }

    @DisplayName("원하는 id의 예약시간을 삭제합니다")
    @Test
    void should_remove_reservation_time_with_exist_id() {
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(
                Arrays.asList(new ReservationTime(1L, LocalTime.of(10, 0)))
        );
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        reservationTimeService.removeReservationTime(1L);

        assertThat(fakeReservationTimeDao.reservationTimes.containsKey(1L)).isFalse();
    }

    @DisplayName("없는 id의 예약시간을 삭제하면 예외를 발생합니다.")
    @Test
    void should_throw_exception_when_remove_reservation_time_with_non_exist_id() {
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao();
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        assertThatThrownBy(() -> reservationTimeService.removeReservationTime(1L)).isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 예약시간이 존재하지 않습니다.");
    }
}
