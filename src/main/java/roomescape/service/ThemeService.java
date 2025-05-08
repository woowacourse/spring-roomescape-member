package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

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

    public ThemeResponseDto saveTheme(ThemeRequestDto request) {
        Theme theme = new Theme(request.name(), request.description(), request.thumbnail());
        themeDao.saveTheme(theme);
        return ThemeResponseDto.from(theme);
    }

    public List<ThemeResponseDto> getAllThemeOfRanks() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(DAYS_TO_SUBTRACT);
        List<Theme> themes = themeDao.findAllThemeOfRankBy(startDate, currentDate, THEME_COUNT_TO_LIMIT);
        return themes.stream().map(ThemeResponseDto::from).toList();
    }

    public void deleteTheme(Long id) {
        themeDao.deleteTheme(id);
    }
}
