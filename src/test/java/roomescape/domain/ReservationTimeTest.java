package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReservationTimeTest {
    @Test
    void null을_입력받으면_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> ReservationTime.of(1, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
