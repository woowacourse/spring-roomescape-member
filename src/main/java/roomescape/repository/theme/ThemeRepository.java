package roomescape.repository.theme;

import java.util.List;
import java.util.Optional;
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

    public Optional<Theme> getTheme(long id) {
        Optional<ThemeDaoData> themeDaoData = themeDao.getTheme(id);

        if(themeDaoData.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(createTheme(themeDao.getTheme(id).get()));
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
