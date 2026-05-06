package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public Theme create(String name, String description, String thumbnail) {
        Theme theme = new Theme(null, name, description, thumbnail);
        Long id = themeDao.insert(theme);
        return themeDao.findBy(id);
    }

    public void delete(Long id) {
        validateId(id);
        themeDao.delete(id);
    }

    public List<Theme> findWeeklyTopTen() {
        LocalDate startDate = LocalDate.now().minusWeeks(1);
        LocalDate endDate = startDate.plusDays(6);
        return themeDao.findPopular(startDate, endDate, 10);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("[ERROR] id는 양수이어야 합니다.");
        }
    }
}
