package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservationtime.dto.request.ReservationTimeRequestDto;

class ReservationTimeRequestDtoTest {

    @DisplayName("잘못된 날짜 형식이 들어온다면, 예외가 발생해야 한다")
    @Test
    void invalid_time_format_then_throw_exception() {
        assertThatThrownBy(() -> new ReservationTimeRequestDto(
            "25:30"
        ));
    }

    @DisplayName("시간이 들어온다면, 성공한다.")
    @Test
    void success_time() {
        assertThatCode(() -> new ReservationTimeRequestDto(
            "13:13"
        ));
    }

}