package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ThemeDao;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.UnauthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class ThemeServiceTest {

    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        ThemeDao themeDao = mock(ThemeDao.class);
        themeService = new ThemeService(themeDao);
    }

    @Test
    void 관리자가_아니면_테마를_생성할_수_없다() {
        assertThatThrownBy(() -> themeService.createTheme(
                new ThemeCreateRequest("공포", "설명", "image.jpg", "USER")))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 관리자가_아니면_테마를_삭제할_수_없다() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L, "USER"))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 없는_테마는_삭제할_수_없다() {


        assertThatThrownBy(() -> themeService.deleteTheme(1L, "ADMIN"))
                .isInstanceOf(ThemeNotFoundException.class);
    }
}
