package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<Theme> findAll() {
        return themeDao.selectAll();
    }

    public Theme findById(Long id) {
        return themeDao.selectById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND));
    }

    public List<Theme> findByTrend(LocalDate from, LocalDate to, int limit) {
        return themeDao.selectByTrend(from, to, limit);
    }

    public Theme addTheme(String name, String description, String image) {
        if (themeDao.existsByName(name)) {
            throw new BusinessException(ErrorCode.THEME_CONFLICT);
        }
        Theme theme = new Theme(name, description, image);
        return themeDao.insert(theme);
    }

    public void removeById(Long id) {
        if (reservationDao.existsByThemeId(id)) {
            throw new BusinessException(ErrorCode.THEME_IN_USE);
        }
        themeDao.deleteById(id);
    }
}
