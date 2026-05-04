package roomescape.repository.theme;

import java.util.List;
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

    public List<Theme> getAllTheme() {
        List<ThemeDaoData> themeDaoAllData = themeDao.getAllTheme();
        return themeDaoAllData.stream()
                .map(this::createTheme)
                .toList();
    }

    public void deleteTheme(long id) {
        themeDao.deleteTheme(id);
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
