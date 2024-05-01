package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(LocalDate.of(12, 12, 12), "dodo", 1L);

        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재 하지 않는 예약시각으로 예약할 수 없습니다.");
    }
}
