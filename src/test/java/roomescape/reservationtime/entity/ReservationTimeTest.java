package roomescape.reservationtime.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    void 예약_시간이_null이면_검증에_실패한다() {
        assertThatThrownBy(() -> ReservationTime.of(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
