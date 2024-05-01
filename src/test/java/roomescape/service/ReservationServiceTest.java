package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationAddRequest;

class ReservationServiceTest {

    @DisplayName("없는 id의 예약을 삭제하면 예외를 발생합니다.")
    @Test
    void should_false_when_remove_reservation_with_non_exist_id() {
        ReservationService reservationService = new ReservationService(new FakeReservationDao(),
                new FakeReservationTimeDao());

        assertThatThrownBy(() -> reservationService.removeReservation(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 예약이 존재하지 않습니다.");
    }

    @DisplayName("존재하지 않는 예약시각으로 예약 시 예외가 발생합니다.")
    @Test
    void should_throw_IllegalArgumentException_when_reserve_non_exist_time() {
        ReservationService reservationService = new ReservationService(new FakeReservationDao(),
                new FakeReservationTimeDao());

        LocalDate reservationDate = LocalDate.now().plusDays(2L);
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(reservationDate, "dodo", 1L);

        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재 하지 않는 예약시각으로 예약할 수 없습니다.");
    }

    @DisplayName("예약 날짜와 예약시각이 같은 경우 예외를 발생합니다.")
    @Test
    void should_throw_IllegalArgumentException_when_reserve_date_and_time_duplicated() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        ReservationService reservationService = new ReservationService(new FakeReservationDao(
                Arrays.asList(new Reservation(1L, "lib", LocalDate.now().plusDays(1), reservationTime))
        ),
                new FakeReservationTimeDao(Arrays.asList(reservationTime)));
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(LocalDate.now().plusDays(1), "dodo",
                1L);

        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜와 예약시간이 겹치는 예약은 할 수 없습니다.");
    }
}
