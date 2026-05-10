package roomescape.user.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.user.dao.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll() {
        return themeDao.selectAll();
    }

    public Theme findById(Long id) {
        return themeDao.selectById(id);
    }

    public List<Theme> findByTrend(LocalDate startDate, LocalDate endDate, int limit) {
        return themeDao.selectByTrend(startDate, endDate, limit);
    }
}
