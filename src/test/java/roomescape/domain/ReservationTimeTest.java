package roomescape.domain;

import static org.assertj.core.api.Assertions.*;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ReservationTimeValidationException;
import roomescape.common.exception.ReservationValidationException;

class ReservationTimeTest {
    @Test
    void validate() {
        // given
        Supplier<ReservationTime> supplier = () -> new ReservationTime(1L, null);

        // when & then
        assertThatThrownBy(supplier::get).isInstanceOf(ReservationTimeValidationException.class);
    }
}
