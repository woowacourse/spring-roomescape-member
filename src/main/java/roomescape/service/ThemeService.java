package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import roomescape.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
                .orElseThrow(() -> new NotFoundException("theme"));
    }

    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.delete(id);
    }

    public List<ReservationTime> getAvailableTimes(Long themeId, LocalDate date) {
        LocalDate selectedDate = date;

        if (Objects.isNull(date)) {
            selectedDate = LocalDate.now();
        }

        return themeRepository.findAvailableTimes(themeId, selectedDate);
    }

    public List<Theme> findPopularThemes() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startInclusive = currentDate.minusDays(8);
        LocalDate endInclusive = currentDate.minusDays(1);

        return themeRepository.findPopularThemes(startInclusive, endInclusive, TOP_NUMBERS);
    }
}
