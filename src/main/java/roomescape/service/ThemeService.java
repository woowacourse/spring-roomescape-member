package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.delete(id);
    }

    public List<ReservationTime> getAvailableTimes(Long themeId, LocalDate date) {
        return themeRepository.findAvailableTimes(themeId, date);
    }

    public List<Theme> findPopularThemes() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startInclusive = currentDate.minusDays(8);
        LocalDate endInclusive = currentDate.minusDays(1);

        return themeRepository.findPopularThemes(startInclusive, endInclusive, TOP_NUMBERS);
    }
}
