package roomescape.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.Theme;
import roomescape.exception.EntityExistsException;
import roomescape.exception.ForeignKeyViolationException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public List<Theme> getLastWeekTop10() {
        return getLastWeekTop10(LocalDate.now());
    }

    public List<Theme> getLastWeekTop10(LocalDate currentDate) {
        return themeDao.getLastWeekTop10(currentDate);
    }

    public Theme create(Theme theme) {
        requireNameNotAlreadyExists(theme.name());
        return themeDao.save(theme);
    }

    public void delete(long id) {
        requireNotReferred(id);
        requireExists(id);
        themeDao.delete(id);
    }

    public boolean existsById(long id) {
        return themeDao.existsById(id);
    }

    public boolean existsByName(String name) {
        return themeDao.existsByName(name);
    }

    private void requireExists(long id) {
        if (!existsById(id)) {
            throw new EntityExistsException("Theme with id " + id + " does not exists.");
        }
    }

    private void requireNotReferred(long id) {
        if (reservationDao.existsByThemeId(id)) {
            throw new ForeignKeyViolationException(
                    "Cannot delete a theme with id " + id + " as being referred by reservation");
        }
    }

    private void requireNameNotAlreadyExists(String name) {
        if (existsByName(name)) {
            throw new EntityExistsException("Theme " + name + " already exists.");
        }
    }
}
