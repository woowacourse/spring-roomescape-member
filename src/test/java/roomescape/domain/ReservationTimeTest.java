package roomescape.domain;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReservationTimeTest {
    @Test
    void null을_입력받으면_예외가_발생한다() {
        String time = null;
        Assertions.assertThatThrownBy(() -> ReservationTime.of(1, LocalTime.parse(time)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
