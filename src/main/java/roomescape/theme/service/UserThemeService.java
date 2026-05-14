package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.InvalidRequestException;
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
        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
        }
        return themeRepository.findRanked(sortColumn, sortOrder, startDate, endDate, limit);
    }

    @Transactional(readOnly = true)
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}
