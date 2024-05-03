package roomescape.domain.theme;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ThemeNameTest {
    @ParameterizedTest
    @NullAndEmptySource
    void 테마명이_비어있으면_예외가_발생한다(String name) {
        assertThatThrownBy(() -> new ThemeName(name))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테마명이 비어있습니다.");
    }

    @Test
    void 테마명이_3글자_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ThemeName("ab"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테마명은 3글자 이상, 20글자 이하여야 합니다.");
    }

    @Test
    void 테마명이_20글자_초과이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ThemeName("abcdefghijabcdefghija"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테마명은 3글자 이상, 20글자 이하여야 합니다.");
    }
}
