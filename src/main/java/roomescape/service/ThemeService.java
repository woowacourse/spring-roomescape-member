package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAllThemes() {
        return themeDao.findAllThemes();
    }

    public Theme addTheme(ThemeRequest request) {
        Theme theme = new Theme(null, request.name(), request.description(), request.thumbnail());
        return themeDao.addTheme(theme);
    }

    public void removeTheme(Long id) {
        themeDao.removeThemeById(id);
    }
}
