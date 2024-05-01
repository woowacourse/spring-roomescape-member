package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> readThemes() {
        return themeDao.readThemes();
    }

    public Theme createTheme(ThemeCreateRequest dto) {
        return themeDao.createTheme(dto.createTheme());
    }
}
