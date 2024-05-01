package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeServiceTest {

    ThemeService themeService = new ThemeService(new FakeThemeRepository());

    @DisplayName("테마를 조회한다.")
    @Test
    void should_find_all_themes() {
        Assertions.assertThat(themeService.findAllThemes()).hasSize(3);
    }
}