package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAllTheme() {
        return themeDao.findAll();
    }

    public Theme addTheme(ThemeAddRequest themeAddRequest) {
        Theme theme = themeAddRequest.toEntity();
        return themeDao.insert(theme);
    }

    public void removeTheme(Long id) {
        if (themeDao.findById(id).isEmpty()) {
            throw new IllegalArgumentException("해당 id를 가진 테마가 존재하지 않습니다.");
        }
        themeDao.deleteById(id);
    }
}
