package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @DisplayName("시간이 형식에 맞지 않을 때 예외를 던진다.")
    @Test
    void validateTimeTest_whenTimeFormatIsNotMatch() {
        String time = "9-00";

        assertThatThrownBy(() -> new ReservationTime(null, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간(%s)이 HH:mm 형식에 맞지 않습니다.".formatted(time));
    }

    @DisplayName("시간이 비어있을 때 예외를 던진다.")
    @Test
    void validateTimeTest_whenTimeIsNull() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("인자 중 null 값이 존재합니다.");
    }
}
