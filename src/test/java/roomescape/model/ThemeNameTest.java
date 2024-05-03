package roomescape.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeNameTest {

    @DisplayName("비어 있는 테마 이름 입력 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"     ", " ", ""})
    void blankThemeName(final String name) {
        assertThatThrownBy(() -> new ThemeName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름이 비어 있습니다.");
    }

    @DisplayName("숫자만으로 이루어진 테마 이름 입력 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"1234", "132314"})
    void numericThemeName(final String name) {
        assertThatThrownBy(() -> new ThemeName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("(%s) 숫자만으로 이루어진 테마 이름은 사용할 수 없습니다.", name));
    }

    @DisplayName("최대 길이를 넘은 테마 이름 입력 시 예외 발생")
    @Test
    void nameLengthOutOfRange() {
        final String name = "a".repeat(51);
        assertThatThrownBy(() -> new ThemeName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("(%s) 테마 이름이 최대 길이인 50자를 넘었습니다.", name));
    }
}
