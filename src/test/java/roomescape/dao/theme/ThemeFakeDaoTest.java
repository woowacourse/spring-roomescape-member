package roomescape.dao.theme;

import roomescape.dao.ThemeDao;
import roomescape.fixture.FakeDatabase;
import roomescape.fixture.FakeThemeDao;

public class ThemeFakeDaoTest extends ThemeDaoContract {

    private final FakeThemeDao themeDao = new FakeThemeDao();

    @Override
    ThemeDao dao() {
        return themeDao;
    }

    @Override
    void clear() {
        FakeDatabase.clearTheme();
    }
}
