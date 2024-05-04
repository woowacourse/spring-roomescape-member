package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ThemeRequest;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAllThemes();
    }

    public Theme addTheme(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.getName(), themeRequest.getDescription(), themeRequest.getThumbnail());
        return themeRepository.addTheme(theme);
    }

    public void deleteTheme(long id) {
        themeRepository.deleteTheme(id);
    }

    public List<Theme> findPopularThemes() {
        LocalDate before = LocalDate.now().minusDays(8);
        LocalDate after = LocalDate.now().minusDays(1);
        return themeRepository.findThemeRankingByDate(before, after, 10);
    }
}
