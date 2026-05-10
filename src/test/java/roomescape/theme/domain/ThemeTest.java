package roomescape.theme.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    void 식별자가_같은_경우_동등한_객체로_판단한다() {
        // given
        Theme themeA = new Theme(1L, "A", "hello", "/resources/image/.../A");
        Theme themeB = new Theme(1L, "B", "hello", "/resources/image/.../B");

        // when & then
        Assertions.assertThat(themeA).isEqualTo(themeB);
    }
}
