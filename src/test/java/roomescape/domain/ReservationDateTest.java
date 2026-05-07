package roomescape.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReservationDateTest {
    @Test
    void null을_입력받으면_예외가_발생한다() {
        String empty = null;
        Assertions.assertThatThrownBy(() -> ReservationDate.from(empty)).isInstanceOf(IllegalArgumentException.class);
    }
}
