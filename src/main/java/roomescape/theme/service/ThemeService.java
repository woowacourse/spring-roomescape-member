package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.DuplicatedDataException;
import roomescape.exception.EmptyDataAccessException;
import roomescape.exception.UnableDeleteDataException;
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
            throw new DuplicatedDataException("이미 해당 테마가 존재합니다.");
        }
        final long id = themeDao.save(theme);
        return Theme.createWithId(id, theme);
    }

    public void deleteById(final long id) {
        if (reservationDao.checkExistReservationByTheme(id)) {
            throw new UnableDeleteDataException("해당 테마를 예약한 예약내역이 존재하여 삭제가 불가합니다.");
        }
        int affectedColumn = themeDao.deleteById(id);
        if (affectedColumn == 0) {
            throw new EmptyDataAccessException("themeId : %d에 해당하는 테마가 존재하지 않습니다.", id);
        }
    }

    public List<Theme> findPopularThemes() {
        final LocalDate today = LocalDate.now();
        final LocalDate yesterDay = today.minusDays(1);
        final LocalDate lastWeek = today.minusWeeks(1);
        return themeDao.findPopularThemes(lastWeek, yesterDay);
    }
}
