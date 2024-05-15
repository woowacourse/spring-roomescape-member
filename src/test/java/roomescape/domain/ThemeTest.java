package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        ThemeName name = new ThemeName("레벨 1 방탈출");
        Description description = new Description("description");
        Thumbnail thumbnail = new Thumbnail("thumbnail");

        assertThatCode(() -> new Theme(name, description, thumbnail))
                .doesNotThrowAnyException();
    }
}
