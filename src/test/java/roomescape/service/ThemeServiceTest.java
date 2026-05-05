package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ThemeDao;
import roomescape.dao.ThemeDaoJdbcDao;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.TimeRequestDto;
import roomescape.service.fake.FakeThemeDao;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeServiceTest {
    private ThemeService themeService;
    private ThemeDao themeDao;

    private void saveThemeHandler(Theme... themes) {
        for (Theme theme : themes) {
            themeDao.insert(theme);
        }
    }

    @BeforeEach
    void setUp() {
        themeDao = new FakeThemeDao();
        themeService = new ThemeService(themeDao);
    }

    @Test
    void 테마을_생성하면_id가_부여된_테마가_생성된다() {
        Theme theme = themeService.create(new ThemeRequestDto("테마1", "www.url.com","테마1 입니다."));

        assertThat(theme).isNotNull();
        assertThat(theme.getId()).isEqualTo(1L);
    }

    @Test
    void 지정한_id를_가진_테마를_조회할_수_있다() {
        Name name1 = new Name("테마1");
        Theme theme1 = new Theme(name1, "www.url1.com", "테마1 입니다");

        Name name2 = new Name("테마2");
        Theme theme2 = new Theme(name2, "www.url2.com", "테마2 입니다");

        saveThemeHandler(theme1, theme2);

        Long id = 2L;
        Theme themeById = themeService.findById(2L);

        assertThat(themeById.getDescription()).isEqualTo(theme2.getDescription());
        assertThat(themeById.getName()).isEqualTo(theme2.getName());
        assertThat(themeById.getThumbnailUrl()).isEqualTo(theme2.getThumbnailUrl());
    }

    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        Long id = 1L;

        assertThatThrownBy(() -> {
            themeService.findById(id);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long id = 1L;

        assertThatThrownBy(() -> {
            themeService.delete(id);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
