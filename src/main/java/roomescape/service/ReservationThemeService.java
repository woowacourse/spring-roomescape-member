package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationThemeDao;
import roomescape.domain.ReservationTheme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;

@Service
public class ReservationThemeService {

    private final ReservationDao reservationDao;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationThemeService(ReservationDao reservationDao, ReservationThemeDao reservationThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationThemeDao = reservationThemeDao;
    }

    public List<ThemeResponse> getAllThemes() {
        return reservationThemeDao.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public ThemeResponse insertTheme(ThemeRequest themeRequest) {
        Long id = reservationThemeDao.insert(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        ReservationTheme inserted = new ReservationTheme(id, themeRequest.name(), themeRequest.description(),
                themeRequest.thumbnail());
        return new ThemeResponse(inserted);
    }

    public void deleteTheme(Long id) {
        if (reservationDao.countByThemeId(id) != 0) {
            throw new IllegalStateException();
        }
        reservationThemeDao.deleteById(id);
    }

    public List<ThemeResponse> getWeeklyBestThemes() {
        LocalDate now = LocalDate.now();
        LocalDate from = now.minusWeeks(1);
        LocalDate to = now.minusDays(1);

        List<Long> bestTheme = reservationDao.findBestThemeIdInWeek(from.toString(), to.toString());
        return bestTheme.stream()
                .map(themeId -> reservationThemeDao.findById(themeId)
                        .orElseThrow(IllegalArgumentException::new))
                .map(ThemeResponse::new)
                .limit(10)
                .toList();
    }
}
