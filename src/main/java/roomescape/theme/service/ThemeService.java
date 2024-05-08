package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequestDto;

@Service
public class ThemeService {
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public Theme save(final ThemeRequestDto request) {
        final Theme theme = request.toTheme();
        if (themeDao.checkExistThemes(theme)) {
            throw new RoomEscapeException(ExceptionMessage.DUPLICATE_THEME);
        }
        final long id = themeDao.save(theme);
        return Theme.createWithId(id, theme);
    }

    public void deleteById(final long id) {
        if (reservationDao.checkExistReservationByTheme(id)) {
            throw new RoomEscapeException(ExceptionMessage.EXIST_REFER_THEME);
        }
        themeDao.deleteById(id);
    }

    public List<Theme> findPopularThemes() {
        final LocalDate today = LocalDate.now();
        final LocalDate yesterDay = today.minusDays(1);
        final LocalDate lastWeek = today.minusWeeks(1);
        return themeDao.findPopularThemes(lastWeek, yesterDay);
    }
}
