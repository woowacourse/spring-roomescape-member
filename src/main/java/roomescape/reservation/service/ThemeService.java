package roomescape.reservation.service;

import java.util.List;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Theme;
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

    public Theme createTheme(Theme theme) {
        try {
            return themeDao.save(theme);
        } catch (DataAccessException e) {
            throw new CustomException(CustomInternalServerError.FAIl_TO_CREATE);
        }
    }

    public Theme findTheme(Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new CustomException(CustomBadRequest.NOT_FOUND_THEME));
    }

    public List<Theme> findAllThemes() {
         return themeDao.findAllThemes();
    }

    public List<Theme> findWeeklyTop10Themes() {
        return findLimitThemes(10);
    }

    private List<Theme> findLimitThemes(int limit) {
        LocalDate now = LocalDate.now(clock);

        LocalDate start = now.minusDays(8);
        LocalDate end = now.minusDays(1);
        return themeDao.findTopReservedThemesByDateRangeAndLimit(start, end, limit);
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
