package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }


    public List<Theme> getThemes() {
        return themeRepository.findAll();
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
}
