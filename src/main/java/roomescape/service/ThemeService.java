package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.UnauthorizedException;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Transactional
    public Theme createTheme(String name, String description, String imgUrl, String userName) {
        if (!userName.equals("ADMIN")) {
            throw new UnauthorizedException();
        }
        Long id = themeDao.insertTheme(name, description, imgUrl);
        return themeDao.findById(id);
    }

    @Transactional
    public void deleteTheme(Long id, String userName) {
        if (!userName.equals("ADMIN")) {
            throw new UnauthorizedException();
        }
        int deleteCount = themeDao.delete(id);
        if (deleteCount == 0) {
            throw new ThemeNotFoundException();
        }
    }
}
