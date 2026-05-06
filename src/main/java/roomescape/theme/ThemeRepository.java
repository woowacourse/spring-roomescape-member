package roomescape.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ThemeRepository {

    private final ThemeDao themeDao;

    public ThemeRepository(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public Theme save(String name, String description, String thumbnail) {
        return themeDao.save(name, description, thumbnail);
    }

    public void delete(long id) {
        themeDao.delete(id);
    }

    public List<Theme> findRanked(String sort, String order, LocalDate startDate, LocalDate endDate, Long limit) {
        return themeDao.findRanked(sort, order, startDate, endDate, limit);
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public Optional<Theme> findById(long id) {
        return themeDao.findById(id);
    }
}
