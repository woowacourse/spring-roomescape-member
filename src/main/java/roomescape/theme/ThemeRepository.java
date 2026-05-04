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

    public long save(String name, String description, String thumbnail) {
        return themeDao.save(name, description, thumbnail);
    }

    public void delete(long id) {
        themeDao.delete(id);
    }

    public List<Theme> findAll(String sort, String order, LocalDate startDate, LocalDate endDate, Long limit) {
        return themeDao.findAll(sort, order, startDate, endDate, limit);
    }

    public Optional<Theme> findById(long id) {
        return themeDao.findById(id);
    }
}
