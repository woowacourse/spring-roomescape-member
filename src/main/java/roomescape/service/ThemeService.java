package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ThemeDAO;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {
    private static final int NUMBER_TO_MAKE_WEEK = 7;
    private static final int NUMBER_TO_MAKE_DAY = 1;
    private static final int TOP_RANKING_LIMIT = 10;

    private final ReservationDAO reservationDAO;
    private final ThemeDAO themeDAO;

    public ThemeService(final ReservationDAO reservationDAO, final ThemeDAO themeDAO) {
        this.reservationDAO = reservationDAO;
        this.themeDAO = themeDAO;
    }

    public Theme save(final ThemeRequest themeRequest) {
        final Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return themeDAO.insert(theme);
    }

    public List<Theme> findAll() {
        return themeDAO.selectAll();
    }

    public void delete(final Long id) {
        if (reservationDAO.existTheme(id)) {
            throw new IllegalStateException("해당 테마에 대한 예약이 존재하여 삭제할 수 없습니다.");
        }
        themeDAO.deleteById(id);
    }

    public List<Theme> findTopRanking() {
        final LocalDate currentDate = LocalDate.now();
        final LocalDate startDate = currentDate.minusDays(NUMBER_TO_MAKE_WEEK);
        final LocalDate endDate = currentDate.minusDays(NUMBER_TO_MAKE_DAY);

        return themeDAO.findTopRankingThemes(startDate, endDate, TOP_RANKING_LIMIT);
    }
}
