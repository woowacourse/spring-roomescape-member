package roomescape.repository.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Component
public class ThemeRepositoryImpl implements ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepositoryImpl(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Override
    public List<Theme> findAllTheme() {
        return themeDao.findAllTheme();
    }

    @Override
    public void saveTheme(Theme theme) {
        long savedId = themeDao.saveTheme(theme);
        theme.setId(savedId);
    }

    @Override
    public void deleteTheme(Long id) {
        findById(id);
        themeDao.deleteTheme(id);
    }

    @Override
    public Theme findById(Long id) {
        return themeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    @Override
    public List<Theme> findAllThemeOfRanks(LocalDate startDate, LocalDate currentDate) {
        return themeDao.findAllThemeOfRanks(startDate, currentDate);
    }
}
