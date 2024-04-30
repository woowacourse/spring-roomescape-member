package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간은 10분 단위이다.")
    void validateTimeUnit() {
        // given
        String invalidTime = "01:23";

        // when & then
        assertThatThrownBy(() -> new ReservationTime(invalidTime))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "13-00"})
    @DisplayName("예약 시간 입력 값이 null이거나 빈 값이면 예외가 발생한다.")
    void convertToLocalTime(String invalidTime) {
        // when & then
        assertThatThrownBy(() -> new ReservationTime(invalidTime))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
