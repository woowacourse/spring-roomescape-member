package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;

@ServiceTest
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("테마를 생성한다.")
    @Test
    void shouldReturnCreatedTheme() {
        ThemeRequest request = new ThemeRequest("테마", "테마 설명", "url");
        themeService.create(request);
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(1);
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void shouldReturnAllThemes() {
        createTheme();
        List<ThemeResponse> themes = themeService.findAll();
        assertThat(themes).hasSize(1);
    }

    @DisplayName("id로 테마를 삭제한다.")
    @Test
    void shouldDeleteThemeWhenDeleteWithId() {
        Theme theme = createTheme();
        themeService.deleteById(theme.getId());
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).isEmpty();
    }

    private Theme createTheme() {
        return themeRepository.create(new Theme(new ThemeName("테마"), "테마 설명", "url"));
    }
}
