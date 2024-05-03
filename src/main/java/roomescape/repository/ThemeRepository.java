package roomescape.repository;

import org.springframework.stereotype.Repository;
import roomescape.model.Theme;
import roomescape.repository.dao.ThemeDao;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepository(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAllThemes() {
        return themeDao.findAllThemes();
    }

    public Theme saveTheme(Theme theme) {
        return themeDao.saveTheme(theme);
    }

    public void deleteThemeById(long id) {
        themeDao.deleteThemeById(id);
    }

    public List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int rankingCount) {
        return themeDao.findThemeRankingByDate(before, after, rankingCount);
    }
}
