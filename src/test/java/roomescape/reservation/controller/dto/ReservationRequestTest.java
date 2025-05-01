package roomescape.reservation.controller.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationRequestTest {

    @DisplayName("이름이 존재하지 않으면 예외가 발생한다")
    @Test
    void name_null_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest(null, LocalDate.of(2025, 4, 25), 1L, 3L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @DisplayName("이름이 공백이면 예외가 발생한다")
    @Test
    void name_blank_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest(" ", LocalDate.of(2025, 4, 25), 1L, 3L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @DisplayName("예약 날짜가 존재하지 않으면 예외가 발생한다")
    @Test
    void date_null_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("루키", null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

    @DisplayName("예약 시간 ID 값이 존재하지 않으면 예외가 발생한다")
    @Test
    void time_id_null_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("루키", LocalDate.of(2025, 4, 25), null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간 ID는 필수입니다.");
    }

}
