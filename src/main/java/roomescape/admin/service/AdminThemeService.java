package roomescape.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.admin.dao.AdminThemeDao;
import roomescape.domain.Theme;

@Service
public class AdminThemeService {

    private final AdminThemeDao adminThemeDao;

    public AdminThemeService(AdminThemeDao adminThemeDao) {
        this.adminThemeDao = adminThemeDao;
    }

    public List<Theme> findAll() {
        return adminThemeDao.selectAll();
    }

    public Theme findById(Long id) {
        return adminThemeDao.selectById(id);
    }

    public Theme addTheme(String name, String description, String image) {
        Theme theme = new Theme(name, description, image);
        return adminThemeDao.insert(theme);
    }

    public void removeById(Long id) {
        adminThemeDao.deleteById(id);
    }
}
