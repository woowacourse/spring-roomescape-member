package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

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
        return themeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 식별자로 데이터를 찾을 수 없습니다. id: " + id));
    }

    public Theme saveTheme(String name, String description, String thumbnailUrl) {
        Theme theme = Theme.transientOf(name, description, thumbnailUrl);
        return themeRepository.save(theme);
    }

    public void removeTheme(long themeId) {
        themeRepository.deleteById(themeId);
    }

    public void putTheme(long id, String name, String description, String thumbnailUrl) {
        themeRepository.update(new Theme(id, name, description, thumbnailUrl));
    }

    public void patchTheme(long id, String name, String description, String thumbnailUrl) {
        Theme theme = findThemeById(id);
        themeRepository.update(theme.patch(name, description, thumbnailUrl));
    }

    public List<Theme> findPopularThemes(Long topCount, Long during) {
        LocalDate toDate = LocalDate.now().minusDays(ONE_DAY);
        LocalDate fromDate = LocalDate.now().minusDays(during);
        return themeRepository.findPopularThemes(topCount, fromDate, toDate);
    }
}
