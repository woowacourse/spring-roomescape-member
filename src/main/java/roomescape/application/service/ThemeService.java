package roomescape.application.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ThemeRequest;
import roomescape.application.dto.ThemeResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

@Service
public class ThemeService {

    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(ThemeDao themeDao, ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeDao.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> findThemeRank() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(8);
        LocalDate endDate = now.minusDays(1);
        List<Long> rank = reservationDao.findRank(startDate, endDate);

        return convertThemeIdsToThemeResponses(rank);
    }

    public ThemeResponse createTheme(ThemeRequest themeRequest) {
        Theme themeWithoutId = themeRequest.toTheme();

        Theme savedTheme = saveTheme(themeWithoutId);
        return new ThemeResponse(savedTheme);
    }

    public void deleteTheme(long id) {
        themeDao.deleteById(id);
    }

    private List<ThemeResponse> convertThemeIdsToThemeResponses(List<Long> rank) {
        return rank.stream().map(themeId -> {
            Theme theme = themeDao.findById(themeId);
            return new ThemeResponse(theme);
        }).toList();
    }

    private Theme saveTheme(Theme themeWithoutId) {
        Long id = themeDao.create(themeWithoutId);
        return themeWithoutId.copyWithId(id);
    }
}
