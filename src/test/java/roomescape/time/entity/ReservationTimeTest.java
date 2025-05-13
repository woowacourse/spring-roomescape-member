package roomescape.time.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("start_at 값이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void error_validationTime() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 예약 시간입니다.");
    }
}
