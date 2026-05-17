package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("시작 시간이 없으면 예약 시간을 생성할 수 없다")
    @Test
    void 시작_시간이_없으면_IllegalArgumentException_예외를_던진다() {
        assertThatThrownBy(() -> ReservationTime.withoutId(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
