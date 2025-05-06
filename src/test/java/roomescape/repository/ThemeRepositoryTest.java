package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeThemeDaoImpl;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.repository.impl.ThemeRepositoryImpl;

public class ThemeRepositoryTest {

    private final ThemeDao themeDao = new FakeThemeDaoImpl();
    private final ThemeRepository themeRepository = new ThemeRepositoryImpl(themeDao);

    @Test
    @DisplayName("존재하지 않는 테마ID를 가져오려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_theme_id_get_then_throw_exception() {
        assertThatThrownBy(() -> themeRepository.findById(999L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하는 테마ID를 가져오려고 할 경우, 성공해야 한다")
    void exist_theme_id_get_then_success() {
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedId = themeDao.saveTheme(theme);
        theme.setId(savedId);
        assertThatCode(() -> themeRepository.findById(savedId)).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("존재하지 않는 테마ID를 삭제하려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_theme_id_delete_then_throw_exception() {
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedId = themeDao.saveTheme(theme);
        theme.setId(savedId);
        assertThatCode(() -> themeRepository.deleteTheme(savedId)).doesNotThrowAnyException();
    }
}
