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

    public List<Theme> getThemes(ThemeSort sort, SortOrder order, LocalDate startDate, LocalDate endDate, Long limit) {
        return themeRepository.findRanked(sort, order, startDate, endDate, limit);
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}
