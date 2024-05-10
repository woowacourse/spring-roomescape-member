package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Limit;
import roomescape.domain.Theme;
import roomescape.domain.VisitDate;
import roomescape.exception.ExistsException;
import roomescape.exception.NotExistsException;
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

    public List<ThemeOutput> findPopularThemes(final String date, final int limit) {
        final List<Theme> themes = themeDao.findPopular(VisitDate.from(date), Limit.from(limit));
        return ThemeOutput.list(themes);
    }

    public void deleteTheme(final long id) {
        themeDao.findById(id)
                .orElseThrow(() -> NotExistsException.of("themeId", id));
        if (reservationDao.isExistByThemeId(id)) {
            throw ExistsException.of(String.format("themeId 가 %d 인 reservation", id));
        }

        themeDao.delete(id);
    }
}
