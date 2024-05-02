package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ThemeDao;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.request.ThemeRequest;
import roomescape.reservation.dto.response.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemeResponse createTheme(ThemeRequest themeRequest) {
        Theme theme = themeDao.save(themeRequest.toEntity());
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeDao.findAllThemes();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findWeeklyTop10Themes() {
        return findLimitThemes(10);
    }

    private List<ThemeResponse> findLimitThemes(int limit) {
        LocalDate now = LocalDate.now();

        LocalDate start = now.minusDays(8);
        LocalDate end = now.minusDays(1);
        return themeDao.findTopReservedThemesByDateRangeAndLimit(start, end, limit).stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        themeDao.delete(id);
    }
}
