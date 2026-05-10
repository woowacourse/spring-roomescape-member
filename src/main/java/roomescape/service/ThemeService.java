package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.config.PopularPeriodProperties;
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
    private final PopularPeriodProperties popularPeriod;

    public ThemeService(ThemeRepository themeRepository, Clock clock, PopularPeriodProperties popularPeriod) {
        this.themeRepository = themeRepository;
        this.clock = clock;
        this.popularPeriod = popularPeriod;
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
        LocalDate startInclusive = currentDate.minusDays(popularPeriod.startDaysAgo());
        LocalDate endInclusive = currentDate.minusDays(popularPeriod.endDaysAgo());

        return themeRepository.findPopularThemes(startInclusive, endInclusive, TOP_NUMBERS);
    }
}
