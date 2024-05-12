package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.domain.Limit;
import roomescape.domain.VisitDate;
import roomescape.repository.ReservationRepository;
import roomescape.service.dto.input.ThemeInput;
import roomescape.service.dto.output.ThemeOutput;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeDao themeDao;

    public ThemeService(final ReservationRepository reservationRepository, final ThemeDao themeDao) {
        this.reservationRepository = reservationRepository;
        this.themeDao = themeDao;
    }

    public ThemeOutput createTheme(final ThemeInput input) {
        final var theme = themeDao.create(input.toTheme());
        return ThemeOutput.from(theme);
    }

    public List<ThemeOutput> getAllThemes() {
        final var themes = themeDao.getAll();
        return ThemeOutput.list(themes);
    }

    public List<ThemeOutput> findPopularThemes(final String date, final int limit) {
        final var themes = themeDao.findPopular(VisitDate.from(date), Limit.from(limit));
        return ThemeOutput.list(themes);
    }

    public void deleteTheme(final long id) {
        final var theme = reservationRepository.getThemeById(id);
        reservationRepository.checkReservationNotExists(theme);
        themeDao.delete(id);
    }
}
