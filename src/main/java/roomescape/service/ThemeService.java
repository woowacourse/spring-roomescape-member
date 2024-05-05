package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.RankTheme;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public Theme create(Theme theme){
        return themeDao.save(theme);
    }

    public void delete(long themeId){
        themeDao.delete(themeId);
    }

    public List<RankTheme> getTop10() {
        return themeDao.getTop10();
    }
}
