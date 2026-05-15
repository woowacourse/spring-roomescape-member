package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

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

    public Theme addTheme(String name, String description, String image) {
        if (themeDao.existsByName(name)) {
            throw new BusinessException(ErrorCode.THEME_CONFLICT);
        }
        Theme theme = new Theme(name, description, image);
        return themeDao.insert(theme);
    }

    public void removeById(Long id) {
        themeDao.deleteById(id);
    }
}
