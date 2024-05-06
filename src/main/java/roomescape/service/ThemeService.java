package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.Theme;
import roomescape.exception.EntityExistsException;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public List<Theme> getTop10() {
        return themeDao.getTop10();
    }

    public Theme create(Theme theme) {
        requireNameNotAlreadyExists(theme.name());
        return themeDao.save(theme);
    }

    public void delete(long id) {
        requireNotAlreadyExists(id);
        themeDao.delete(id);
    }

    public boolean existsById(long id) {
        return themeDao.existsById(id);
    }

    public boolean existsByName(String name) {
        return themeDao.existsByName(name);
    }

    private void requireNotAlreadyExists(long id) {
        if (existsById(id)) {
            throw new EntityExistsException("Theme with id " + id + " already exists");
        }
    }

    private void requireNameNotAlreadyExists(String name) {
        if (existsByName(name)) {
            throw new EntityExistsException("Theme with name " + name + " already exists");
        }
    }
}
