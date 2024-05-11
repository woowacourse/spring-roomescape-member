package roomescape.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberNameTest {

    @DisplayName("예약자 이름에 공백 입력 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"     ", " ", ""})
    void blankName(final String value) {
        assertThatThrownBy(() -> new MemberName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("멤버 이름이 비어 있습니다.");
    }

    @DisplayName("이름에 숫자만 입력 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"123", "456"})
    void numericName(final String value) {
        assertThatThrownBy(() -> new MemberName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("(%s) 숫자만으로 이루어진 멤버 이름은 사용할 수 없습니다.", value));
    }

    @DisplayName("최대 길이를 넘은 예약 이름 입력 시 예외 발생")
    @Test
    void reservationNameOutOfRange() {
        final String value = "b".repeat(31);
        assertThatThrownBy(() -> new MemberName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("(%s) 멤버 이름이 최대 길이인 30자를 넘었습니다.", value));
    }

    @DisplayName("올바른 이름 입력")
    @ParameterizedTest
    @ValueSource(strings = {"abc123", "감자", "Joo Woo"})
    void validName(final String value) {
        assertThatCode(() -> new MemberName(value))
                .doesNotThrowAnyException();
    }
}
