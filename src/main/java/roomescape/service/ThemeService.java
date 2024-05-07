package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.VisitDate;
import roomescape.exception.ExistReservationInThemeException;
import roomescape.exception.NotExistThemeException;
import roomescape.service.dto.input.ThemeInput;
import roomescape.service.dto.output.ThemeOutput;

@Service
public class ThemeService {

    final ThemeDao themeDao;
    final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeOutput createTheme(final ThemeInput input) {
        final Theme theme = themeDao.create(input.toTheme());
        return ThemeOutput.from(theme);
    }

    public List<ThemeOutput> getAllThemes() {
        final List<Theme> themes = themeDao.getAll();
        return ThemeOutput.list(themes);
    }

    public List<ThemeOutput> findPopularThemes(final String date) {
        final List<Theme> themes = themeDao.findPopular(VisitDate.from(date));
        return ThemeOutput.list(themes);
    }

    public void deleteTheme(final long id) {
        themeDao.find(id)
                .orElseThrow(() -> new NotExistThemeException(id));
        if (reservationDao.isExistByThemeId(id)) {
            throw new ExistReservationInThemeException(id);
        }

        themeDao.delete(id);
    }
}
