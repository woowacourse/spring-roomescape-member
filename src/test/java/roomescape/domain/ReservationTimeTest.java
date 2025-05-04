package roomescape.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    void 시작_시간이_null이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> {
            new ReservationTime(
                    null
            );
        }).isInstanceOf(NullPointerException.class);
    }
}
