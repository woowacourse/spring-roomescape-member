package roomescape.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ThemeDao;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.request.ThemeRequest;
import roomescape.reservation.dto.response.ThemeResponse;
import roomescape.reservation.handler.exception.CustomBadRequest;
import roomescape.reservation.handler.exception.CustomException;
import roomescape.reservation.handler.exception.CustomInternalServerError;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    private final Clock clock;

    public ThemeService(ThemeDao themeDao, Clock clock) {
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public ThemeResponse createTheme(ThemeRequest themeRequest) {
        try {
            Theme theme = themeDao.save(themeRequest.toEntity());
            return ThemeResponse.from(theme);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIl_TO_CREATE);
        }
    }

    public List<ThemeResponse> findAllThemes() {
        List<Theme> themes = themeDao.findAllThemes();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findWeeklyTop10Themes() {
        return findLimitThemes(10);
    }

    private List<ThemeResponse> findLimitThemes(int limit) {
        LocalDate now = LocalDate.now(clock);

        LocalDate start = now.minusDays(8);
        LocalDate end = now.minusDays(1);
        return themeDao.findTopReservedThemesByDateRangeAndLimit(start, end, limit).stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteTheme(Long id) {
        try {
            themeDao.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(CustomBadRequest.THEME_IN_USE);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIL_TO_REMOVE);
        }
    }
}
