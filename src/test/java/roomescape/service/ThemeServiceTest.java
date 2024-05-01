package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.repository.ThemeDao;

class ThemeServiceTest {


    @DisplayName("존재하지 않는 테마 삭제 요청시 예외가 발생합니다")
    @Test
    void should_throw_IllegalArgumentException_when_theme_id_no_exist() {
        ThemeDao themeDao = new FakeThemeDao();
        ThemeService themeService = new ThemeService(themeDao);

        assertThatThrownBy(() -> themeService.removeTheme(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 테마가 존재하지 않습니다.");
    }
}
