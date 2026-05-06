package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.ThemeRequestDto;
import roomescape.service.fake.FakeThemeDao;

public class ThemeServiceTest {
    private ThemeService themeService;
    private ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        themeDao = new FakeThemeDao();
        themeService = new ThemeService(themeDao);
    }

    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsThemeId = 1L;

        assertThatThrownBy(() -> themeService.findById(notExistsThemeId))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsThemeId= 1L;

        assertThatThrownBy(() -> themeService.delete(notExistsThemeId))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
