package roomescape.domain.theme;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ThemeNameTest {
    @Test
    void 이름의_길이가_3글자_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ThemeName("ab"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테마명 길이는 3글자 이상, 20글자 이하여야 합니다.");
    }

    @Test
    void 이름의_길이가_20글자_초과이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ThemeName("abcdefghijabcdefghija"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테마명 길이는 3글자 이상, 20글자 이하여야 합니다.");
    }
}
