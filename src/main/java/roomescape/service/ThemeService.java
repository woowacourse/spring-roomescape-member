package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AlreadyExistException;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

@Service
@Transactional
public class ThemeService {
    private static final int POPULAR_THEME_PERIOD_DAYS = 6;

    private final ThemeDao themeDao;
    private final Clock clock;

    public ThemeService(ThemeDao themeDao, Clock clock) {
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public ThemeResponse addTheme(ThemeRequest request) {
        validateUniqueTheme(request.name());
        Theme savedTheme = themeDao.insert(request.toTheme());
        return ThemeResponse.from(savedTheme);
    }

    private void validateUniqueTheme(String name) {
        boolean exists = themeDao.existsByName(name);
        if (exists) {
            throw new AlreadyExistException("이미 존재하는 테마입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> getThemes() {
        return themeDao.selectAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> getPopularThemes() {
        LocalDate endDate = LocalDate.now(clock);
        LocalDate startDate = endDate.minusDays(POPULAR_THEME_PERIOD_DAYS);

        List<Theme> popularThemes = themeDao.selectPopularThemesByPeriod(startDate, endDate.minusDays(1));
        return popularThemes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void delete(long themeId) {
        int deleted = themeDao.delete(themeId);
        if (deleted == 0) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }
}
