package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme saveTheme(Theme theme) {
        long themeId = themeRepository.save(theme);

        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 없습니다."));
    }

    public List<Theme> findThemeList() {
        List<Theme> themes = themeRepository.findAll();
        validateThemeExists(themes);

        return themes;
    }

    public void deleteThemeById(long id) {
        int deleteCount = themeRepository.deleteById(id);

        validateDeletionOccurred(deleteCount);
    }

    public List<Theme> findPopularThemeList() {
        LocalDate today = LocalDate.now();

        List<Theme> themes = themeRepository.findPopular(today.minusWeeks(1), today.minusDays(1));
        validateThemeExists(themes);

        return themes;
    }

    private void validateThemeExists(List<Theme> themes) {
        if (themes.isEmpty()) {
            throw new IllegalArgumentException("테마가 없습니다.");
        }
    }

    private void validateDeletionOccurred(int deleteCount) {
        if (deleteCount == 0) {
            throw new NoSuchElementException("해당하는 테마가 없습니다.");
        }
    }
}
