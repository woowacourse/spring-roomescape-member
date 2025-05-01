package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.exception.DuplicateThemeException;

@Service
public class ThemeService {

    public static final int TOP_RANK_PERIOD_DAYS = 7;
    public static final int TOP_RANK_THRESHOLD = 10;

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAllThemes() {
        return themeDao.findAllThemes();
    }

    public List<Theme> findTopReservedThemes() {
        LocalDate today = LocalDate.now();
        return themeDao.findTopReservedThemesInPeriodWithLimit(today.minusDays(TOP_RANK_PERIOD_DAYS), today,
            TOP_RANK_THRESHOLD);
    }

    public Theme addTheme(ThemeRequest request) {
        validateDuplicateTheme(request);
        Theme theme = new Theme(null, request.name(), request.description(), request.thumbnail());
        return themeDao.addTheme(theme);
    }

    private void validateDuplicateTheme(ThemeRequest themeRequest) {
        if (themeDao.existThemeByName(themeRequest.name())) {
            throw new DuplicateThemeException();
        }
    }

    public void removeTheme(Long id) {
        themeDao.removeThemeById(id);
    }
}
