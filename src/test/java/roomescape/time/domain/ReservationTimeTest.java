package roomescape.time.domain;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    void 시간이_null이면_예외가_발생한다() {
        // given
        final LocalTime time = null;

        // when & then
        Assertions.assertThatThrownBy(() -> new ReservationTime(time))
                .isInstanceOf(IllegalArgumentException.class);
    }
}