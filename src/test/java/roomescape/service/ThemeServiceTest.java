package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.request.ThemeRequest;

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
        themeService.addTheme(themeRequest);
        assertThat(themeService.findAllThemes()).hasSize(4);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void should_delete_theme() {
        themeService.deleteTheme(1L);
        assertThat(themeService.findAllThemes()).hasSize(2);
    }
}