package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.theme.Theme;
import roomescape.theme.dao.FakeThemeDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.ThemeResponse;

public class ThemeServiceTest {

    private final ThemeService themeService;
    private final ThemeDao themeDao;

    public ThemeServiceTest() {
        Theme theme1 = new Theme(1L, "name1", "description1", "thumbnail");
        Theme theme2 = new Theme(2L, "name1", "description1", "thumbnail");
        this.themeDao = new FakeThemeDao(theme1, theme2);
        this.themeService = new ThemeService(themeDao);
    }

    @Test
    void 테마를_생성할_수_있다() {
        // given
        ThemeRequest theme = new ThemeRequest("name3", "description3", "thumbnail3");
        // when
        ThemeResponse savedTheme = themeService.create(theme);
        // then
        assertThat(savedTheme.name()).isEqualTo("name3");
        assertThat(savedTheme.description()).isEqualTo("description3");
        assertThat(savedTheme.thumbnail()).isEqualTo("thumbnail3");
    }

    @Test
    void 테마_목록을_조회할_수_있다() {
        // when
        List<ThemeResponse> themes = themeService.findAll();
        // then
        assertThat(themes).hasSize(2);
    }

    @Test
    void 테마를_삭제할_수_있다() {
        // when
        themeService.delete(1L);
        // then
        assertThat(themeDao.findAll()).hasSize(1);
    }
}
