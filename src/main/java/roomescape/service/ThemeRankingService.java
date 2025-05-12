package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRanking;

@Service
public class ThemeRankingService {

    private static final int THEME_RANKING_END_RANGE = 7;
    private static final int THEME_RANKING_START_RANGE = 1;

    private final ReservationService reservationService;

    public ThemeRankingService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public List<Theme> getRankingThemes(LocalDate originDate) {
        LocalDate end = originDate.minusDays(THEME_RANKING_START_RANGE);
        LocalDate start = end.minusDays(THEME_RANKING_END_RANGE);

        List<Reservation> inRangeReservations = reservationService.findAllByDateInRange(start, end);

        ThemeRanking themeRanking = new ThemeRanking(inRangeReservations);
        return themeRanking.getAscendingRanking();
    }
}
