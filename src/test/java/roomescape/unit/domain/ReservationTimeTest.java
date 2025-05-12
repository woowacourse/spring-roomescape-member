package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.*;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;

class ReservationTimeTest {
    @Test
    void validate() {
        // given
        Supplier<ReservationTime> supplier = () -> new ReservationTime(1L, null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                supplier::get
        );
    }
}
