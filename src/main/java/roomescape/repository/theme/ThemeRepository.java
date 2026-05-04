package roomescape.repository.theme;

import org.springframework.stereotype.Repository;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeDaoData;

@Repository
public class ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepository(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Theme addTheme(ThemeCommand themeCommand) {
        ThemeDaoData themeDaoData = themeDao.addTheme(themeCommand);
        return createTheme(themeDaoData);
    }

    private Theme createTheme(ThemeDaoData themeDaoData) {
        return new Theme(
                themeDaoData.id(),
                themeDaoData.name(),
                themeDaoData.description(),
                themeDaoData.imageUrl()
        );
    }
}
