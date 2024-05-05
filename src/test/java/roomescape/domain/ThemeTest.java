package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.application.exception.EntityCreationException;

class ThemeTest {

    @Test
    @DisplayName("테마 설명이 200자를 초과하면 예외가 발생한다")
    void descriptionLengthTest() {
        ThemeName name = new ThemeName("테마명");
        String description = ".".repeat(201);
        assertThatCode(() -> new Theme(name, description, "url"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명은 200자 이하여야 합니다.");
    }

    @Test
    @DisplayName("테마 썸네일 URL이 200자를 초과하면 예외가 발생한다")
    void urlLengthTest() {
        ThemeName name = new ThemeName("테마명");
        String url = "a".repeat(201);
        assertThatCode(() -> new Theme(name, "description", url))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 썸네일 URL은 200자 이하여야 합니다.");
    }
}
