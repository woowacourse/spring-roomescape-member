package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.ThemeName;

class ThemeNameTest {

    @DisplayName("이름이 비어있거나 null이면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenNameIsNullOrEmpty(String input) {
        assertThatCode(() -> new ThemeName(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마명은 필수 입력값 입니다.");
    }

    @DisplayName("이름이 20자를 초과하면 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenNameLengthExceededMaxLength() {
        String input = "-".repeat(21);
        assertThatCode(() -> new ThemeName(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마명은 20자 이하여야 합니다.");
    }

    @DisplayName("이름이 올바르게 생성된다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "12345678901234567890"})
    void shouldCreateName(String input) {
        assertThatCode(() -> new ThemeName(input))
                .doesNotThrowAnyException();
    }
}
