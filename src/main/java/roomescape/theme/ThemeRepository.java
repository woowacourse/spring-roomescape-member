package roomescape.theme;

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
}
