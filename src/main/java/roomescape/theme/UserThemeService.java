package roomescape.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserThemeService {

    private final ThemeRepository themeRepository;

    public UserThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<Theme> getThemes(String sort, String order, LocalDate startDate, LocalDate endDate, Long limit) {
        return themeRepository.findRanked(sort, order, startDate, endDate, limit);
    }

    @Transactional(readOnly = true)
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}
