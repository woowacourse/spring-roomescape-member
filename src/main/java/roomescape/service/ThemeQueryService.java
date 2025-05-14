package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.response.ThemeFullResponse;
import roomescape.entity.Theme;

@Service
public class ThemeQueryService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeQueryService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeFullResponse> findAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeFullResponse::new)
                .toList();
    }

    public List<ThemeFullResponse> findThemeRank() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(8);
        LocalDate endDate = now.minusDays(1);
        List<Long> themeIds = reservationDao.findMostReservedThemeIdsBetween(startDate, endDate);
        List<Theme> themes = themeDao.findAllById(themeIds);

        return themes.stream()
                .map(ThemeFullResponse::new)
                .toList();
    }
}
