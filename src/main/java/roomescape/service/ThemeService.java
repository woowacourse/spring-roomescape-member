package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Theme createTheme(CreateThemeRequest request) {
        Long newThemeId = themeDao.save(request);
        return themeDao.findById(newThemeId);
    }

    public void deleteTheme(Long id) {
        themeDao.deleteById(id);
    }
}
