package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ThemeRequest;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepositoryImpl;

@Service
public class ThemeService {

    private final ThemeRepositoryImpl themeRepositoryImpl;

    public ThemeService(ThemeRepositoryImpl themeRepositoryImpl) {
        this.themeRepositoryImpl = themeRepositoryImpl;
    }

    public List<Theme> findAllThemes() {
        return themeRepositoryImpl.findAllThemes();
    }

    public Theme addTheme(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return themeRepositoryImpl.addTheme(theme);
    }

    public void deleteTheme(long id) {
        themeRepositoryImpl.deleteTheme(id);
    }

    public List<Theme> findPopularThemes() {
        LocalDate before = LocalDate.now().minusDays(8);
        LocalDate after = LocalDate.now().minusDays(1);
        return themeRepositoryImpl.findThemeRankingByDate(before, after, 10);
    }
}
