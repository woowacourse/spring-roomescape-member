package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Limit;
import roomescape.domain.Theme;
import roomescape.domain.VisitDate;
import roomescape.exception.CustomBadRequest;
import roomescape.service.dto.input.ThemeInput;
import roomescape.service.dto.output.ThemeOutput;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao,
                        final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeOutput createTheme(final ThemeInput input) {
        final var theme = themeDao.create(input.toTheme());
        return ThemeOutput.from(theme);
    }

    public List<ThemeOutput> getAllThemes() {
        final var themes = themeDao.getAll();
        return ThemeOutput.list(themes);
    }

    public Theme getThemeById(final Long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new CustomBadRequest(String.format("themeId(%s)가 존재하지 않습니다.", themeId)));
    }

    public List<ThemeOutput> findPopularThemes(final String date, final int limit) {
        final var themes = themeDao.findPopular(VisitDate.from(date), Limit.from(limit));
        return ThemeOutput.list(themes);
    }

    public void deleteTheme(final long id) {
        final var theme = getThemeById(id);
        checkReservationNotExists(theme);
        themeDao.delete(id);
    }

    public void checkReservationNotExists(final Theme theme) {
        if (reservationDao.exists(theme)) {
            throw new CustomBadRequest(String.format("예약(themeId=%s)이 존재합니다.", theme.id()));
        }
    }
}
