package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationNameTest {
    @DisplayName("예약자 이름이 null일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsNull() {
        assertThatThrownBy(() -> new ReservationName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("예약자 이름이 \"\"일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsEmpty() {
        assertThatThrownBy(() -> new ReservationName(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
