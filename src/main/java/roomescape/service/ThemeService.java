package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.exception.NotFoundException;
import roomescape.repository.ThemeRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private static final int TOP_NUMBERS = 10;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository, Clock clock) {
        this.themeRepository = themeRepository;
        this.clock = clock;
    }


    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("theme"));
    }

    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.delete(id);
    }

    public List<Theme> findPopularThemes() {
        LocalDate currentDate = LocalDate.now(clock);
        LocalDate startInclusive = currentDate.minusDays(7);
        LocalDate endInclusive = currentDate.minusDays(1);

        return themeRepository.findPopularThemes(startInclusive, endInclusive, TOP_NUMBERS);
    }
}
