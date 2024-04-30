package roomescape.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @DisplayName("비어 있는 날짜 입력")
    @Test
    void emptyDate() {
        assertThatThrownBy(() ->
                new Reservation(1L, "감자", "", new ReservationTime(1L, "10:00")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
