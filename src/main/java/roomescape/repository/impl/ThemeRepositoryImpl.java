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
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    @Override
    public void save(Theme theme) {
        long savedId = themeDao.save(theme);
        theme.setId(savedId);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        themeDao.delete(id);
    }

    @Override
    public Theme findById(Long id) {
        return themeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마를 찾을 수 없습니다"));
    }

    @Override
    public List<Theme> findAllOfRank(LocalDate startDate, LocalDate currentDate) {
        return themeDao.findAllOfRanks(startDate, currentDate);
    }
}
