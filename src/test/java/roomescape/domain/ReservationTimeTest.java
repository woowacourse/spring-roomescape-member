package roomescape.domain;

import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {
    @Test
    void validate() {
        // given
        Supplier<ReservationTime> supplier = () -> new ReservationTime(1L, null);

        // when & then
        Assertions.assertThatNullPointerException().isThrownBy(
                supplier::get
        );
    }
}
