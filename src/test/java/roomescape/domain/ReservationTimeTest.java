package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("startAt이 null이면 예외를 던진다")
    void startAt_not_null() {
        assertThatThrownBy(() -> {
            new ReservationTime(
                null
            );
        }).isInstanceOf(NullPointerException.class);
    }
}
