package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AlreadyExistException;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.UnprocessableException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

@Service
@Transactional
public class ThemeService {
    private static final int POPULAR_THEME_PERIOD_DAYS = 6;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;
    private final Clock clock;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao, Clock clock) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
        this.clock = clock;
    }

    public ThemeResponse addTheme(ThemeRequest request) {
        validateUniqueTheme(request.name());
        Theme savedTheme = themeDao.insert(request.toTheme());
        return ThemeResponse.from(savedTheme);
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

    public void deleteTheme(long themeId) {
        Optional<Theme> theme = themeDao.selectById(themeId);
        if (theme.isEmpty()) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }

        validateThemeIncludeReservation(themeId);
        themeDao.delete(themeId);
    }

    private void validateUniqueTheme(String name) {
        boolean exists = themeDao.existsByName(name);
        if (exists) {
            throw new AlreadyExistException("이미 존재하는 테마입니다.");
        }
    }

    private void validateThemeIncludeReservation(long themeId) {
        boolean existsByThemeId = reservationDao.existsByThemeId(themeId);
        if (existsByThemeId) {
            throw new UnprocessableException("예약된 테마는 삭제할 수 없습니다.");
        }
    }
}
