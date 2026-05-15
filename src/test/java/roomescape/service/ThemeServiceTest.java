package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ThemeDao;
import roomescape.exception.ThemeInUseException;
import roomescape.exception.ThemeNotFoundException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ThemeServiceTest {

    private ThemeDao themeDao;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeDao = mock(ThemeDao.class);
        themeService = new ThemeService(themeDao);
    }

    @Test
    void 없는_테마는_삭제할_수_없다() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessage("해당 테마를 찾을 수 없습니다.");
    }

    @Test
    void 예약이_있는_테마는_삭제할_수_없다() {
        when(themeDao.delete(1L)).thenThrow(new ThemeInUseException("해당 테마에 예약이 존재합니다."));

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(ThemeInUseException.class)
                .hasMessage("해당 테마에 예약이 존재합니다.");

        verify(themeDao).delete(1L);
    }
}
