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
                new Reservation("감자", null, new ReservationTime(LocalTime.parse("10:00")), new Theme("이름", "설명", "썸네일")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
