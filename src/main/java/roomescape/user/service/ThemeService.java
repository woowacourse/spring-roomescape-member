package roomescape.user.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.user.dao.ThemeDao;
import roomescape.domain.Theme;

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

    public List<Theme> findByTrend(LocalDate from, LocalDate to, int limit) {
        return themeDao.selectByTrend(from, to, limit);
    }
}
