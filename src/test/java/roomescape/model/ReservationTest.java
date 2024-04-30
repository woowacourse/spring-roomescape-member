package roomescape.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("비어 있는 날짜 입력")
    @Test
    void emptyDate() {
        assertThatThrownBy(() ->
                new Reservation(1L, "감자", null, new ReservationTime(1L, LocalTime.parse("10:00"))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
