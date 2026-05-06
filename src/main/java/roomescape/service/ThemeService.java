package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

@Service
public class ThemeService {
    private static final int DATE_DIFFERENCE = 7;

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse addTheme(ThemeRequest request) {
        Theme theme = request.toTheme();
        validateTheme(theme);
        Theme savedTheme = themeDao.insert(theme);
        return ThemeResponse.from(savedTheme);
    }

    private void validateTheme(Theme theme) {
        Optional<Theme> newTheme = themeDao.selectById(theme.getId());
        if (newTheme.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 테마입니다.");
        }
    }

    public List<ThemeResponse> getThemes() {
        return themeDao.selectAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> getPopularThemes(LocalDate endDate) {
        LocalDate startDate = endDate.minusDays(DATE_DIFFERENCE);
        List<Theme> popularThemes = themeDao.selectPopularThemesByPeriod(startDate, endDate);
        return popularThemes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void delete(long themeId) {
        themeDao.selectById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        themeDao.delete(themeId);
    }
}
