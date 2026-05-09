package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ThemeReservationTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private static final int POPULAR_THEME_PERIOD_DAYS = 7;
    private static final int POPULAR_THEME_END_OFFSET_DAYS = 1;
    private static final int MAX_POPULAR_THEMES_LIMIT = 10;
    private static final int MIN_POPULAR_THEMES_LIMIT = 0;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final Clock clock;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                        Clock clock) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.clock = clock;
    }

    public List<Theme> getThemes() {
        return themeDao.findAll();
    }

    public Theme createTheme(CreateThemeRequest request) {
        Long newThemeId = themeDao.save(request);
        return themeDao.findById(newThemeId);
    }

    public void deleteTheme(Long id) {
        themeDao.deleteById(id);
    }

    public List<ThemeReservationTimeResponse> getThemeTimes(Long themeId, LocalDate date) {
        List<Long> reservedTimeIds = reservationDao.findTimeIdsByThemeIdAndDate(themeId, date);
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        return reservationTimes.stream()
                .map(time -> new ThemeReservationTimeResponse(
                        time.getId(),
                        time.getStartAt().toString(),
                        reservedTimeIds.contains(time.getId())
                ))
                .toList();
    }

    public List<PopularThemeResponse> getPopularThemes(Integer limit) {
        limit = Math.clamp(limit, MIN_POPULAR_THEMES_LIMIT, MAX_POPULAR_THEMES_LIMIT);
        LocalDate today = LocalDate.now(clock);
        LocalDate to = today.minusDays(POPULAR_THEME_END_OFFSET_DAYS);
        LocalDate from = today.minusDays(POPULAR_THEME_PERIOD_DAYS);

        return themeDao.findPopularThemes(from, to, limit);
    }
}
