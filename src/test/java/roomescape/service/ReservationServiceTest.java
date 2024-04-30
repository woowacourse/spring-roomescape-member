package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
