package roomescape.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeNameTest {

    @Test
    void 같은_이름을_가진_객체는_동등한_객체이다() {
        //given
        ThemeName name = new ThemeName("코로구");
        ThemeName sameValueName = new ThemeName("코로구");

        // when, then
        assertThat(name).isEqualTo(sameValueName);
    }

    @Test
    void 같은_이름을_가진_객체는_동등하지_않은_객체이다() {
        //given
        ThemeName name = new ThemeName("코로구");
        ThemeName differentValueName = new ThemeName("파도");

        // when, then
        assertThat(name).isNotEqualTo(differentValueName);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "\t\n"})
    void 빈_문자열로_생성하면_예외가_발생한다(String value) {
        // when & then
        assertThatThrownBy(() -> new ThemeName(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("빈 문자열은");
    }
}