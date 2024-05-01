package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTimeTest {
    @DisplayName("정상 생성 테스트")
    @Test
    void validCreate() {
        assertDoesNotThrow(() -> new ReservationTime(LocalTime.parse("10:00")));
    }

    @DisplayName("빈문자열이 들어왔을 때 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void EmptyValueThrowsException(String value) {
        assertThatThrownBy(() -> new ReservationTime(LocalTime.parse(value)))
                .isInstanceOf(DateTimeParseException.class);
    }

    @DisplayName("null이 들어왔을 때 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void nullThrowsException(String value) {
        assertThatThrownBy(() -> new ReservationTime(LocalTime.parse(value)))
                .isInstanceOf(NullPointerException.class);
    }
}
