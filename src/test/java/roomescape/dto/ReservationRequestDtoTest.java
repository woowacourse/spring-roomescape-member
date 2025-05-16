package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.request.ReservationRequestDto;

class ReservationRequestDtoTest {

    @Test
    @DisplayName("빈 날짜가 들어온다면, 예외가 발생해야 한다")
    void invalid_date_then_throw_exception() {
        assertThatThrownBy(() -> new ReservationRequestDto(
            null,
            1L,
            1L
        ));
    }

    @Test
    @DisplayName("잘못된 날짜 형식이 들어온다면, 예외가 발생해야 한다")
    void invalid_date_format_then_throw_exception() {
        assertThatThrownBy(() -> new ReservationRequestDto(
            "2024-13-12",
            1L,
            1L
        ));
    }

    @Test
    @DisplayName("빈 시간ID가 들어온다면, 예외가 발생해야 한다")
    void invalid_time_id_then_throw_exception() {
        assertThatThrownBy(() -> new ReservationRequestDto(
            "2024-12-02",
            null,
            1L
        ));
    }

    @Test
    @DisplayName("이름이 들어온다면, 성공한다.")
    void success_name() {
        assertThatCode(() -> new ReservationRequestDto(
            "2024-12-02",
            1L,
            1L
        ));
    }
}