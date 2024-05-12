package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThatCode(() -> new Theme(new ThemeName("레벨 1 방탈출"), "description", "thumbnail"))
                .doesNotThrowAnyException();
    }
}
