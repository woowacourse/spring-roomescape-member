package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

@Service
public class ThemeService {
    private static final int POPULAR_THEME_PERIOD_DAYS = 7;
    private static final int BASE_DATE_EXCLUDED_DAYS = 1;

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse addTheme(ThemeRequest request) {
        validateUniqueTheme(request.name());
        Theme savedTheme = themeDao.insert(request.toTheme());
        return ThemeResponse.from(savedTheme);
    }

    private void validateUniqueTheme(String name) {
        boolean exists = themeDao.existsByName(name);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 테마입니다.");
        }
    }

    public List<ThemeResponse> getThemes() {
        return themeDao.selectAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> getPopularThemes(LocalDate baseDate) {
        LocalDate startDate = baseDate.minusDays(POPULAR_THEME_PERIOD_DAYS);
        LocalDate endDate = baseDate.minusDays(BASE_DATE_EXCLUDED_DAYS);
        List<Theme> popularThemes = themeDao.selectPopularThemesByPeriod(startDate, endDate);
        return popularThemes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void delete(long themeId) {
        validateThemeExists(themeId);
        themeDao.delete(themeId);
    }

    private void validateThemeExists(Long themeId) {
        boolean exists = themeDao.existsById(themeId);
        if (!exists) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }
}
