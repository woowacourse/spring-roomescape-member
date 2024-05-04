package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeNameTest {
    @DisplayName("테마 이름이 null일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsNull() {
        assertThatThrownBy(() -> new ThemeName(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("테마 이름이 \"\"일 경우 예외를 던진다.")
    @Test
    void validateTest_whenValueIsEmpty() {
        assertThatThrownBy(() -> new ThemeName(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 한글자 이상이어야 합니다.");
    }
}
