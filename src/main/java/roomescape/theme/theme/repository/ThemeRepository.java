package roomescape.theme.theme.repository;

import org.springframework.stereotype.Repository;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.theme.dao.ThemeDao;
import roomescape.theme.theme.domain.Theme;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static roomescape.global.exception.ExceptionMessage.THEME_NOT_FOUND;

@Repository
public class ThemeRepository {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ThemeRepository(ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<Theme> findBest10Between(LocalDate startDate, LocalDate endDate) {
        List<Long> themeIds = reservationDao.find10ThemeIdsOrderByReservationThemeCountDescBetween(startDate, endDate);
        return themeIds.stream()
                .map(this::getTheme)
                .toList();
    }

    private Theme getTheme(Long id) {
        return themeDao.findById(id)
                .orElseThrow(() -> new RoomEscapeException(NOT_FOUND, THEME_NOT_FOUND.getMessage()));
    }
}
