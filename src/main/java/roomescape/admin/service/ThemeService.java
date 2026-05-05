package roomescape.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.admin.dao.ThemeDao;
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

    public Theme add(String name, String description, String image) {
        Theme theme = new Theme(name, description, image);
        return themeDao.insert(theme);
    }
}
