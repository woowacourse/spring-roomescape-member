package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int ONE_DAY = 1;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> allTheme() {
        return themeRepository.findAll();
    }

    public Theme findThemeById(long id) {
        return themeRepository.findById(id);
    }

    public Theme saveTheme(String name, String description, String thumbnailUrl) {
        Theme theme = Theme.transientOf(name, description, thumbnailUrl);
        return themeRepository.save(theme);
    }

    public void removeTheme(long themeId) {
        themeRepository.deleteById(themeId);
    }

    public List<Theme> findPopularThemes(Long topCount, Long during) {
        LocalDate toDate = LocalDate.now().minusDays(ONE_DAY);
        LocalDate fromDate = LocalDate.now().minusDays(during);
        return themeRepository.findPopularThemes(topCount, fromDate, toDate);
    }
}
