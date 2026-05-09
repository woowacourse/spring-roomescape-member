package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepository(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public Theme save(Theme theme) {
        return themeDao.save(theme);
    }

    public void deleteById(Long id) {
        themeDao.deleteById(id);
    }

    public Optional<Theme> findById(Long id) {
        return themeDao.findById(id);
    }

    public List<Theme> getPopularThemes(LocalDate start, LocalDate end, Integer limit) {
        return themeDao.getPopularThemes(start, end, limit);
    }
}
