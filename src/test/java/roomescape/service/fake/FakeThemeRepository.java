package roomescape.service.fake;

import java.time.LocalDate;
import java.util.List;

import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final FakeThemeDao themeDao;

    public FakeThemeRepository(FakeThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public void clear() {
        themeDao.clear();
    }

    @Override
    public List<Theme> findAllThemes() {
        return themeDao.findAllThemes();
    }

    @Override
    public Theme addTheme(Theme theme) {
        return themeDao.addTheme(theme);
    }

    @Override
    public void deleteTheme(long id) {
        themeDao.deleteTheme(id);
    }

    @Override
    public List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int limit) {
        return themeDao.findThemeRankingByDate(before, after, limit);
    }
}
