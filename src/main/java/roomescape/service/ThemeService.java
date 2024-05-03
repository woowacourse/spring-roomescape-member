package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ThemeDto;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllThemes() {
        return themeRepository.findAllThemes();
    }

    public Theme saveTheme(ThemeDto themeDto) {
        Theme theme = Theme.from(themeDto);
        return themeRepository.saveTheme(theme);
    }

    public void deleteTheme(long id) {
        themeRepository.deleteThemeById(id);
    }

    public List<Theme> findPopularThemes() {
        LocalDate before = LocalDate.now().minusDays(8);
        LocalDate after = LocalDate.now().minusDays(1);
        return themeRepository.findThemeRankingByDate(before, after, 10);
    }
}
