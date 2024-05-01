package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<Theme> readThemes() {
        return themeDao.readThemes();
    }

    public Theme createTheme(ThemeCreateRequest dto) {
        if (themeDao.isExistThemeByName(dto.name())) {
            throw new IllegalArgumentException("해당 테마 이름은 이미 존재합니다.");
        }
        return themeDao.createTheme(dto.createTheme());
    }

    public void deleteTheme(Long id) {
        if (reservationDao.isExistReservationByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마를 사용하는 예약이 존재합니다.");
        }
        themeDao.deleteTheme(id);
    }
}
