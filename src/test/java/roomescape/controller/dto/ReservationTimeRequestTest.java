package roomescape.controller.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.exception.InvalidRequestException;

class ReservationTimeRequestTest {

    @Test
    @DisplayName("시작 시간이 유효하면 요청을 생성한다")
    void 시작_시간이_유효하면_요청을_생성한다() {
        assertDoesNotThrow(() -> new ReservationTimeRequest(LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("시작 시간이 null이면 예외가 발생한다")
    void 시작_시간이_null이면_예외가_발생한다() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> new ReservationTimeRequest(null)
        );
        assertEquals("예약 시간은 필수입니다", exception.getMessage());
    }
}
