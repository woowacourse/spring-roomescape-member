package roomescape.theme;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserThemeService {

    private final ThemeRepository themeRepository;

    public UserThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getThemes(LocalDate startDate, LocalDate endDate, ThemeSort sort, SortOrder order, Long limit) {
        return themeRepository.findRanked(startDate, endDate, sort, order, limit);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}
