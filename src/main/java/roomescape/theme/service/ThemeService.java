package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.RoomEscapeException;
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
            throw new RoomEscapeException("이미 해당 테마가 존재합니다.");
        }
        return themeDao.save(theme);
    }

    public void deleteById(final long id) {
        if (reservationDao.checkExistReservationByTheme(id)) {
            throw new RoomEscapeException("해당 테마를 예약한 예약내역이 존재하여 삭제가 불가합니다.");
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
