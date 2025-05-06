package roomescape.fake;

import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

import java.util.List;

public class FakeThemeRepository implements ThemeRepository {

    private final FakeReservationDao reservationDao;
    private final FakeThemeDao themeDao;

    public FakeThemeRepository() {
        this.reservationDao = new FakeReservationDao();
        this.themeDao = new FakeThemeDao();
    }

    @Override
    public Theme save(final Theme theme) {
        return themeDao.save(theme);
    }

    @Override
    public Theme findById(final Long themeId) {
        return themeDao.findById(themeId);
    }

    @Override
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    @Override
    public int deleteById(final Long id) {
        return themeDao.deleteById(id);
    }

    @Override
    public List<Theme> findPopular(final int count) {
        return themeDao.findPopular(reservationDao.findAll(), count);
    }

    @Override
    public boolean existByName(final String name) {
        return themeDao.existByName(name);
    }

    public void clear() {
        reservationDao.clear();
        themeDao.clear();
    }
}
