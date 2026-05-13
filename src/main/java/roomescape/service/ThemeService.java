package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Period;
import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotFoundException;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {
    private static final int TOP_NUMBERS = 10;

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }


    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }

    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.delete(id);
    }

    public List<Theme> findPopularThemes(Period period) {
        return themeRepository.findPopularThemes(period.startInclusive(), period.endInclusive(), TOP_NUMBERS);
    }
}
