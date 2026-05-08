package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ThemeDao;

@Service
@RequiredArgsConstructor
public class ThemeQueryService {

    private final ThemeDao themeDao;

    public List<ThemeResponse> findAllThemes() {
        return themeDao.findAllThemes().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findPopularThemesByPeriod(LocalDate today, int limit) {
        LocalDate startAt = today.minusWeeks(1);
        LocalDate endAt = today.minusDays(1);

        return themeDao.findSortedPopularThemesBy(startAt, endAt, limit).stream()
                .map(ThemeResponse::from)
                .toList() ;
    }
}
