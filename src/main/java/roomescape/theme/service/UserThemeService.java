package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.controller.SortColumn;
import roomescape.theme.controller.SortOrder;
import roomescape.theme.repository.ThemeRepository;

@Service
public class UserThemeService {

    private final ThemeRepository themeRepository;

    public UserThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<Theme> getThemes(SortColumn sortColumn, SortOrder sortOrder, LocalDate startDate, LocalDate endDate, Long limit) {
        return themeRepository.findRanked(sortColumn, sortOrder, startDate, endDate, limit);
    }

    @Transactional(readOnly = true)
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}
