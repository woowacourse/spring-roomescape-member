package roomescape.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import roomescape.model.Theme;

@Repository
public class ThemeRepositoryImpl {

    private final ThemeDao themeDao;

    public ThemeRepositoryImpl(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAllThemes() {
        return themeDao.findAllThemes();

    }

    public Theme addTheme(Theme theme) {
        return themeDao.addTheme(theme);
    }

    public void deleteTheme(long id) {
        themeDao.deleteTheme(id);
    }

    public Theme findThemeById(long id) {
        return themeDao.findThemeById(id);
    }

    public List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int limit) {
        return themeDao.findThemeRankingByDate(before, after, limit);
    }
}
