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
        if (themeDao.isThemeByName(dto.name())) {
            throw new IllegalArgumentException("해당 테마 이름은 이미 존재합니다.");
        }
        return themeDao.createTheme(dto.createTheme());
    }

    public void deleteTheme(Long id) {
        themeDao.deleteTheme(id);
    }
}
