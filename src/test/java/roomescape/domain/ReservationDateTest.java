package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReservationDateTest {
    @Test
    void null을_입력받으면_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> ReservationDate.from(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
