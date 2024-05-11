package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.dto.ThemesResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(final ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public ThemesResponse findAllThemes() {
        List<ThemeResponse> response = themeDao.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();

        return new ThemesResponse(response);
    }

    public ThemesResponse findTopNThemesBetweenDate(
            final int count, final LocalDate startDate, final LocalDate endDate) {

        List<ThemeResponse> response = themeDao.findByStartDateAndEndDateWithLimit(startDate, endDate, count)
                .stream()
                .map(ThemeResponse::from)
                .toList();

        return new ThemesResponse(response);
    }

    public ThemeResponse addTheme(final ThemeRequest request) {
        Theme theme = themeDao.insert(new Theme(request.name(), request.description(), request.thumbnail()));

        return ThemeResponse.from(theme);
    }

    public void removeThemeById(final Long id) {
        themeDao.deleteById(id);
    }
}
