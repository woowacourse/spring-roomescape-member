package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRankResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {

    public static final int NUMEBER_OF_ONE_WEEK = 7;
    public static final int NUMBER_OF_ONE_DAY = 1;
    public static final int TOP_THEMES_LIMIT = 10;

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeResponse addTheme(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toTheme();
        Theme savedTheme = themeDao.save(theme);
        return ThemeResponse.fromTheme(savedTheme);
    }

    public void removeTheme(long id) {
        themeDao.deleteById(id);
    }

    public List<ThemeResponse> findThemes() {
        List<Theme> themes = themeDao.findAllThemes();
        return themes.stream()
                .map(ThemeResponse::fromTheme)
                .toList();
    }

    public List<ThemeRankResponse> findRankedThemes() {
        LocalDate today = LocalDate.now()
                .minusDays(NUMBER_OF_ONE_DAY);
        LocalDate beforeOneWeek = today.minusDays(NUMEBER_OF_ONE_WEEK);

        List<Theme> rankedThemes = reservationDao.findThemeByDateOrderByThemeIdCount(beforeOneWeek, today);
        System.out.println(rankedThemes);
        return rankedThemes.stream()
                .sequential()
                .limit(TOP_THEMES_LIMIT)
                .map(ThemeRankResponse::fromTheme)
                .toList();
    }

}
