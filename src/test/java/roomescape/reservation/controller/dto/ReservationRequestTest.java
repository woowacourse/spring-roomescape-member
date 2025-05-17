package roomescape.reservation.controller.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationRequestTest {

    @DisplayName("멤버 ID가 존재하지 않으면 예외가 발생한다")
    @Test
    void name_null_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2025, 4, 25), 1L, 3L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("멤버 ID는 필수입니다.");
    }

    @DisplayName("예약 날짜가 존재하지 않으면 예외가 발생한다")
    @Test
    void date_null_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest(null, 1L, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

    @DisplayName("예약 시간 ID 값이 존재하지 않으면 예외가 발생한다")
    @Test
    void time_id_null_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2025, 4, 25), null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간 ID는 필수입니다.");
    }

}
