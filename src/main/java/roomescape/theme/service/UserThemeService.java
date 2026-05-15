package roomescape.theme.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.SortOrder;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.domain.ThemeSort;

@Service
@Transactional(readOnly = true)
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
