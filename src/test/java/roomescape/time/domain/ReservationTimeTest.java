package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("startAt에 null이 입력되면 IllegalArgumentException 발생")
    void constructorNullTimeFormatException() {
        assertThatThrownBy(() -> new ReservationTime( null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Start time cannot be null");
    }
}
