package roomescape.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @DisplayName("비어 있는 시간 입력")
    @Test
    void emptyTime() {
        assertThatThrownBy(() -> new ReservationTime(1L, ""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
