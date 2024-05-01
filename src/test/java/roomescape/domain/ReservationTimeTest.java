package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("time이 null인 경우 ReservationTime 생성 시 예외가 발생합니다")
    @Test
    void should_throw_NPE_when_time_is_null() {
        assertThatThrownBy(() -> new ReservationTime(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("예약 가능 시각은 null일 수 없습니다");
    }
}
