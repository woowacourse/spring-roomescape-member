package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.theme.ThemesResponse;

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
