package roomescape.reservation.controller.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.error.InvalidRequestException;

class ReservationTimeRequestTest {

    @DisplayName("시작 시간이 존재하지 않는 경우 예외가 발생한다")
    @Test
    void start_at_null_exception() {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeRequest(null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("시작 시각은 필수입니다.");
    }

}
