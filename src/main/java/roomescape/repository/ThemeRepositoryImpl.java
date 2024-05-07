package roomescape.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import roomescape.model.Theme;
import roomescape.repository.dao.ThemeDao;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepositoryImpl(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

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
