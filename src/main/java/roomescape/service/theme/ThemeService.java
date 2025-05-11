package roomescape.service.theme;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.theme.ThemeDao;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.request.ThemeRequestDto;
import roomescape.dto.theme.response.ThemeResponseDto;

@Service
public class ThemeService {

    private static final int THEME_COUNT_TO_LIMIT = 10;
    private static final int DAYS_TO_SUBTRACT = 7;

    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponseDto> getAllThemes() {
        return themeDao.findAllTheme().stream().map(ThemeResponseDto::from).toList();
    }

    public ThemeResponseDto saveTheme(final ThemeRequestDto request) {
        final Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        themeDao.saveTheme(theme);
        return ThemeResponseDto.from(theme);
    }

    public List<ThemeResponseDto> getAllThemeOfRanks() {
        final LocalDate currentDate = LocalDate.now();
        final LocalDate startDate = currentDate.minusDays(DAYS_TO_SUBTRACT);
        final List<Theme> themes = themeDao.findAllThemeOfRankBy(startDate, currentDate, THEME_COUNT_TO_LIMIT);
        return themes.stream().map(ThemeResponseDto::from).toList();
    }

    public void deleteTheme(final Long id) {
        themeDao.deleteTheme(id);
    }
}
