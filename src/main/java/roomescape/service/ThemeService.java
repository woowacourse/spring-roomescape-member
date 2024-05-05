package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.VisitDate;
import roomescape.exception.ExistReservationException;
import roomescape.exception.NotExistException;
import roomescape.service.dto.input.ThemeInput;
import roomescape.service.dto.output.ThemeOutput;

import static roomescape.exception.ExceptionDomainType.THEME;

@Service
public class ThemeService {

    final ThemeDao themeDao;
    final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeOutput createTheme(final ThemeInput input) {
        final Theme theme = themeDao.create(Theme.of(null, input.name(), input.description(), input.thumbnail()));
        return ThemeOutput.toOutput(theme);
    }

    public List<ThemeOutput> getAllThemes() {
        final List<Theme> themes = themeDao.getAll();
        return ThemeOutput.toOutputs(themes);
    }

    public List<ThemeOutput> getPopularThemes(final String date) {
        final List<Theme> themes = themeDao.getPopularWeekendTheme(VisitDate.from(date));
        return ThemeOutput.toOutputs(themes);
    }

    public void deleteTheme(final long id) {
        Theme theme = themeDao.find(id)
                              .orElseThrow(() -> new NotExistException(THEME, id));
        if (reservationDao.isExistByThemeId(theme.getId())) {
            throw new ExistReservationException(THEME, theme.getId());
        }

        themeDao.delete(theme.getId());
    }
}
