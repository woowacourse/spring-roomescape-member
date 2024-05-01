package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.request.ThemeRequest;
import roomescape.model.Theme;

import static org.assertj.core.api.Assertions.*;

class ThemeServiceTest {

    ThemeService themeService = new ThemeService(new FakeThemeRepository());

    @DisplayName("테마를 조회한다.")
    @Test
    void should_find_all_themes() {
        assertThat(themeService.findAllThemes()).hasSize(3);
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void should_add_theme() {
        ThemeRequest themeRequest = new ThemeRequest("에버", "공포", "공포.jpg");
        Theme theme = themeService.addTheme(themeRequest);
        assertThat(themeService.findAllThemes()).hasSize(4);
    }
}