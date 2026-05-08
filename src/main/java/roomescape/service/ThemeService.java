package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.AvailableTime;
import roomescape.domain.Theme;
import roomescape.dao.ThemeDao;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> getPopularThemes(int size, LocalDate startDate, LocalDate endDate) {
        return themeDao.findPopularThemes(size, startDate, endDate);
    }

    public List<Theme> getAllThemes() {
        return themeDao.findAll();
    }

    public List<AvailableTime> getAvailableTimes(long themeId, LocalDate date) {
        return themeDao.findAvailableTimeById(themeId, date);
    }
}