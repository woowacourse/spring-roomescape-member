package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

@Component
public class ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepository(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public Long save(Theme theme) {
        return themeDao.save(theme);
    }

    public void deleteById(Long id) {
        themeDao.deleteById(id);
    }

    public Optional<Theme> findById(Long id) {
        return themeDao.findById(id);
    }
}
