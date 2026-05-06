package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ReservationTimeStatusResponse;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationTimeService reservationTimeService;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationTimeService reservationTimeService, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationTimeService = reservationTimeService;
        this.reservationDao = reservationDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public List<PopularThemeResponse> findPopularThemes() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().minusDays(1);
        return themeDao.findPopularThemes(startDate, endDate)
                .stream()
                .map(popularTheme -> PopularThemeResponse.of(
                        popularTheme.getTheme(),
                        popularTheme.getReservationCount())
                )
                .toList();
    }

    public Theme save(Theme theme) {
        return themeDao.save(theme);
    }

    public void deleteById(Long id) {
        themeDao.deleteById(id);
    }

    public List<ReservationTimeStatusResponse> findReservationTimeByDateAndThemeId(LocalDate date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();
        List<Long> timeIds = reservationDao.findReservedTimeIdsByDateAndThemeId(date, themeId);

        return reservationTimes.stream()
                .map(reservationTime -> ReservationTimeStatusResponse.of(
                        reservationTime,
                        !timeIds.contains(reservationTime.getId())))
                .toList();
    }

}
